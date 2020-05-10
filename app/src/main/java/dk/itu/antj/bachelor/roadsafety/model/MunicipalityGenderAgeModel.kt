package dk.itu.antj.bachelor.roadsafety.model

import android.content.Context
import java.io.BufferedReader

class MunicipalityGenderAgeModel(context: Context) {
    var model:MutableMap<String, DoubleArray> = mutableMapOf()

    init {
        val inputStream = context.assets.open("csv/municipalityAgeModel.csv")
        val reader = BufferedReader(inputStream.reader())
        reader.use {
            var line = it.readLine()
            if(line!=null){
                //First line
                line = it.readLine()
            }
            while (line != null) {
                val fields = line.split(',')
                val key = fields[0]
                val values = DoubleArray(fields.lastIndex) {
                    fields[it+1].toDouble()
                }
                model[key] = values
                line = it.readLine()
            }
        }
    }

    fun runModel(parameterDTO: ParameterDTO): Double? {
        return (model[parameterDTO.municipality]
            ?.get(parametersModelInput(parameterDTO.genderMale,parameterDTO.age)))?.times(100)
    }

    private fun parametersModelInput(genderMale:Boolean, age:Int):Int{
        return when{
            age<18  -> throw ModelException("User need to be 18 years or older")
            age<25  -> if(genderMale) 0 else 1
            age<45  -> if(genderMale) 2 else 3
            age<65  -> if(genderMale) 4 else 5
            else    -> if(genderMale) 6 else 7
        }
    }
}