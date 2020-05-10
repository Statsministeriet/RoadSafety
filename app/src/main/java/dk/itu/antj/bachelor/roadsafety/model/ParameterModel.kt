package dk.itu.antj.bachelor.roadsafety.model

import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dk.itu.antj.bachelor.roadsafety.Constants
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*

class ParameterModel(context: Context, parameterCallback: (parameterDTO: ParameterDTO) -> Unit) {
    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private val client = OkHttpClient()


    init {
        val sharedPref = context.getSharedPreferences(Constants.FILE_NAME, Context.MODE_PRIVATE)
        val firstName = sharedPref.getString(Constants.CURRENT_USER_LOGIN_FIRSTNAME,null)
            ?: throw ModelException("Can't get user first name")
        val gender = sharedPref.getBoolean(Constants.CURRENT_USER_LOGIN_MALE,false)
        val year = sharedPref.getInt(Constants.CURRENT_USER_LOGIN_BIRTH_YEAR,0)
        val month = sharedPref.getInt(Constants.CURRENT_USER_LOGIN_BIRTH_MONTH,99)
        val day = sharedPref.getInt(Constants.CURRENT_USER_LOGIN_BIRTH_DAY,99)
        if(year==0||month==99||day==99) throw ModelException("Can't get user age")
        val age = calculateUserAge(year,month,day)

        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
            val longitude = location?.longitude
            val latitude = location?.latitude
            val speed = location?.speed ?: throw ModelException("Can't get speed")
            runRequest("https://dawa.aws.dk/adgangsadresser/reverse?x="+longitude.toString()+"&y="+latitude.toString()) { result ->
                if(result!==null){
                    val municipality = JSONObject(result).getJSONObject("kommune").get("navn")
                    val zone = JSONObject(result).getString("zone")
                    //Byzone, Sommerhusområde eller Landzone
                    val zoneBool =  zone != "Lanzone"
                    runRequest("https://api.openweathermap.org/data/2.5/weather?lat="+latitude.toString()+"&lon="+longitude.toString()+"&appid=95374b3811dd724a06a90aab5cc7155b"){tempResult ->

                        val temperature = JSONObject(tempResult).getJSONObject("main").get("temp")
                        val temperatureCelsius = (temperature.toString().toDouble())-273.15
                        val sunrise = JSONObject(tempResult).getJSONObject("sys").get("sunrise")
                        val sunset =JSONObject(tempResult).getJSONObject("sys").get("sunset")
                        val sunsetting = calculateSunSetting(sunrise.toString().toDouble(), sunset.toString().toDouble())
                        val parameterDTO = ParameterDTO(
                            age=age,
                            firstName = firstName,
                            municipality = municipality.toString(),
                            speed = speed.toInt(),
                            urban = zoneBool,
                            genderMale = gender,
                            temperature = temperatureCelsius,
                            sunUp = sunsetting)
                        parameterCallback.invoke(parameterDTO)

                    }
                }else{
                    throw ModelException("Can't get DAWA result")
                }
            }
        }.addOnFailureListener {
            throw ModelException("Can't get location")
        }

    }

    private fun runRequest(url: String,  myCallback: (result: String?) -> Unit) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) = myCallback(response.body()?.string())
        })
    }
    private fun calculateUserAge(_year:Int, _month:Int, _day:Int):Int{
        val today = Calendar.getInstance()
        val month = if(today.get(Calendar.DAY_OF_MONTH)>=_day) _month+1 else _month
        val year = if(today.get(Calendar.MONTH)>=month)  _year+1 else _year
        return today.get(Calendar.YEAR)-year
    }
    private fun calculateSunSetting(sunrise:Double, sunset:Double):Boolean{
        val currentTime = (System.currentTimeMillis() / 1000L).toDouble()
        return if(sunset.compareTo(currentTime) < 0) {
            false
        } else sunrise<currentTime

    }
}