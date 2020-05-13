package dk.itu.antj.bachelor.roadsafety.model

import dk.itu.antj.bachelor.roadsafety.Constants
import kotlin.math.sqrt

class Predictor(name:String) {
    val name = name
    var data = ArrayList<Double>()
    var counter = 0
    var sum = 0.0
    private fun mean():Double{
        return sum/counter
    }
    fun higher(z:Double):Double{
        var variance1 = 0.0
        for (i in 0 until counter) {
            variance1 += (data[i] - mean()) * (data[i] - mean())
        }
        val variance = variance1 / (counter - 1)

        val sd = sqrt(variance)
        return mean() + (z * sd)
    }
    private fun addData(value: Double){
        data.add(value)
        counter++
        sum += value
    }
    fun addDataAndGetHighValue(value: Double, z:Double):Double{
        data.add(value)
        counter++
        sum += value
        return (higher(z))-value
    }
    fun addDataAndEvaluate(value: Double, z: Double):Boolean{
        val predicate = value>higher(z)
        addData(value)
        return if(Constants.INIT_EVALUATIONS>=counter) false
        else predicate
    }
}