package dk.itu.antj.bachelor.roadsafety.model

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dk.itu.antj.bachelor.roadsafety.Constants
import dk.itu.antj.bachelor.roadsafety.R

class MainModel(context: Context){
    val mainHandler = Handler(Looper.getMainLooper())
    var run = false;
    var counter = 0
    private var municipalityUrbanModel:MunicipalityUrbanModel = MunicipalityUrbanModel()
    private var speedUrbanModel:SpeedUrbanModel = SpeedUrbanModel()
    private var municipalityGenderAgeModel:MunicipalityGenderAgeModel = MunicipalityGenderAgeModel(context)
    private var weatherModel:WeatherModel = WeatherModel(context)

    private var lastRisk = ""

    var accidentPrediction = Predictor("Accident")
    private var accidentTypePredication:List<Predictor> = List(10){
        Predictor(Constants.ACCIDENT_TYPES[it])
    }

    fun onStart(context:Context){
        run = true
        mainHandler.post(object : Runnable {
            override fun run() {
                val sharedPref = context.getSharedPreferences(Constants.FILE_NAME, Context.MODE_PRIVATE)
                val interval = sharedPref.getLong(Constants.SELECTED_FREQUENCY_INTERPRETER, 10000.toLong())
                val zVal = sharedPref.getLong(Constants.Z_VALUE_INTERPRETER, Constants.Z_VALUE_95.toLong())
                runModel(context, zVal.toDouble())
                if(run)mainHandler.postDelayed(this, interval)
                counter++

            }
        })
    }

    fun onStop(){
        run =  false
    }

    fun runModel(context: Context, z: Double) {
        ParameterModel(context){parameterDTO ->
            runPredictions(parameterDTO, context, z)
        }

    }

    fun runPredictions(parameterDTO:ParameterDTO, context: Context, z: Double){
        speedUrbanModel.runModel(parameterDTO){
            val speedUrbanPredictions = it
            municipalityUrbanModel.runModel(parameterDTO){
                val municipalityUrbanPredictions = it
                val municipalityGenderAgePrediction = municipalityGenderAgeModel.runModel(parameterDTO)!!
                val weatherModelPrediction = weatherModel.runModel(parameterDTO)!!
                val accidentPredicate = accidentPrediction.addDataAndEvaluate((municipalityGenderAgePrediction+weatherModelPrediction)/2, z)

                if (accidentPredicate){
                    val accidentType = getHighestRiskAccidentType(municipalityUrbanPredictions,speedUrbanPredictions, z).second
                    if(lastRisk==""){
                        lastRisk = accidentType
                        val notificationBuilder = NotificationCompat.Builder(context, Constants.FILE_NAME)
                            .setSmallIcon(R.drawable.alert2)
                            .setContentTitle("RISK OF ACCIDENT")
                            .setStyle(NotificationCompat.BigTextStyle()
                                .bigText("Be careful for $accidentType"))
                        with(NotificationManagerCompat.from(context)) {
                            // notificationId is a unique int for each notification that you must define
                            notify(counter, notificationBuilder.build())
                        }
                    }
                }else{
                    lastRisk = ""
                }

            }
        }
    }

    fun getHighestRiskAccidentType(municipalityUrbanPredictions:FloatArray, speedUrbanPredictions:FloatArray,z: Double):Pair<Double,String>{
        var highestRisk = Pair(-100.0, "none")
        for(i in 0..(Constants.ACCIDENT_TYPES.size-1)){
            val fst = accidentTypePredication[i].addDataAndGetHighValue(municipalityUrbanPredictions[i].toDouble(),z)
            val snd = accidentTypePredication[i].addDataAndGetHighValue(speedUrbanPredictions[i].toDouble(),z)
            if(fst>highestRisk.first) highestRisk = Pair(fst,Constants.ACCIDENT_TYPES[i])
            if(snd>highestRisk.first) highestRisk = Pair(snd,Constants.ACCIDENT_TYPES[i])
        }
        return highestRisk
    }
}

