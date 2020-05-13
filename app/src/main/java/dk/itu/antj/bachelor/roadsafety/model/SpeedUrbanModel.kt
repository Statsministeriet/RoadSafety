package dk.itu.antj.bachelor.roadsafety.model

import com.google.firebase.ml.custom.*

class SpeedUrbanModel {
    private val testInput = intArrayOf(0, 0, 1, 0, 0, 0, 0, 0, 0, 0)
    private var interpreter : FirebaseModelInterpreter?
    private var inputOutputOptions : FirebaseModelInputOutputOptions?

    init {
        val localModel = FirebaseCustomLocalModel.Builder()
            .setAssetFilePath("tf/speedUrban.tflite")
            .build()
        val options = FirebaseModelInterpreterOptions.Builder(localModel).build()
        interpreter = FirebaseModelInterpreter.getInstance(options)
        inputOutputOptions = FirebaseModelInputOutputOptions.Builder()
            .setInputFormat(0, FirebaseModelDataType.FLOAT32, intArrayOf(1,10))
            .setOutputFormat(0, FirebaseModelDataType.FLOAT32, intArrayOf(1,10))
            .build()
    }

    fun runModel(parameterDTO: ParameterDTO, callback: (result: FloatArray) -> Unit){
        val floatInputs = parametersModelInput(parameterDTO.speed, parameterDTO.urban).map{it.toFloat()}
        val tmpInput = Array(1){ FloatArray(99) }
        tmpInput[0] = floatInputs.toFloatArray()
        val inputs = FirebaseModelInputs.Builder()
            .add(tmpInput)
            .build()
        inputOutputOptions?.let {
            interpreter?.run(inputs, it)?.addOnSuccessListener { result ->
                val output = result.getOutput<Array<FloatArray>>(0)
                callback.invoke(output[0])
            }?.addOnFailureListener { e ->
                throw ModelException("Error evaluating result of municipalityUrbanModel")
            }
        }
    }
    private fun parametersModelInput(speed:Int, urban:Boolean): IntArray {
        val urbanByte = if(urban) 1 else 0
        return when{
            speed <= 20 ->    intArrayOf(1, 0, 0, 0, 0, 0, 0, 0, 0, urbanByte)
            speed <= 50 ->    intArrayOf(0, 1, 0, 0, 0, 0, 0, 0, 0, urbanByte)
            speed <= 60 ->    intArrayOf(0, 0, 1, 0, 0, 0, 0, 0, 0, urbanByte)
            speed <= 70 ->    intArrayOf(0, 0, 0, 1, 0, 0, 0, 0, 0, urbanByte)
            speed <= 80 ->    intArrayOf(0, 0, 0, 0, 1, 0, 0, 0, 0, urbanByte)
            speed <= 90 ->    intArrayOf(0, 0, 0, 0, 0, 1, 0, 0, 0, urbanByte)
            speed <= 100 ->   intArrayOf(0, 0, 0, 0, 0, 0, 1, 0, 0, urbanByte)
            speed <= 110 ->   intArrayOf(0, 0, 0, 0, 0, 0, 0, 1, 0, urbanByte)
            else -> intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 1, urbanByte)
        }
    }
}