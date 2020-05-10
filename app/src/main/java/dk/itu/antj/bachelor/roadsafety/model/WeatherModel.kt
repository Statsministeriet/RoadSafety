package dk.itu.antj.bachelor.roadsafety.model

import android.content.Context
import java.io.BufferedReader

class WeatherModel(context: Context) {
    var model:MutableMap<Int, DoubleArray> = mutableMapOf()

    init {
        val inputStream = context.assets.open("csv/weatherAccidentsModel.csv")
        val reader = BufferedReader(inputStream.reader())
        reader.use {
            var line = it.readLine()
            if(line!=null){
                //First line
                line = it.readLine()
            }
            while (line != null) {
                val fields = line.split(',')
                val key = fields[0].toInt()
                val values = DoubleArray(fields.lastIndex) {
                    fields[it+1].toDouble()
                }
                model[key] = values
                line = it.readLine()
            }
        }
    }

    fun runModel(parameterDTO: ParameterDTO): Double? {
        val temp = if(parameterDTO.temperature>25) 25 else if(parameterDTO.temperature<-4) -4 else parameterDTO.temperature
        println(model[temp]?.size)
        return if(parameterDTO.sunUp){
            (model[temp]?.get(1))
        } else (model[temp]?.get(0))
    }
}