package dk.itu.antj.bachelor.roadsafety.model

import android.content.Context
import kotlin.properties.Delegates

class MainModel(context: Context){
    private var municipalityUrbanModel:MunicipalityUrbanModel = MunicipalityUrbanModel()
    private var speedUrbanModel:SpeedUrbanModel = SpeedUrbanModel()
    private var municipalityGenderAgeModel:MunicipalityGenderAgeModel = MunicipalityGenderAgeModel(context)
    private var weatherModel:WeatherModel = WeatherModel(context)

    private lateinit var speedUrbanPredictions:FloatArray
    private lateinit var municipalityUrbanPredictions:FloatArray
    private var municipalityGenderAgePrediction by Delegates.notNull<Double>()
    private var weatherModelPrediction by Delegates.notNull<Double>()

    fun runModel(context: Context) {
        try {
            ParameterModel(context){parameterDTO ->
                speedUrbanModel.runModel(parameterDTO){
                    speedUrbanPredictions = it
                    municipalityUrbanModel.runModel(parameterDTO){
                        municipalityUrbanPredictions = it
                        municipalityGenderAgePrediction = municipalityGenderAgeModel.runModel(parameterDTO)!!
                        weatherModelPrediction = weatherModel.runModel(parameterDTO)!!

                    }
                }
            }
        }catch (e:ModelException){
            TODO("Update this")
            println(e)
        }
    }

    fun calculateSD(numArray: DoubleArray): Double {
        var sum = 0.0
        var standardDeviation = 0.0

        for (num in numArray) {
            sum += num
        }

        val mean = sum / 10

        for (num in numArray) {
            standardDeviation += Math.pow(num - mean, 2.0)
        }

        return Math.sqrt(standardDeviation / 10)
    }
}

