package dk.itu.antj.bachelor.roadsafety.model

import com.google.firebase.ml.custom.*

class MunicipalityUrbanModel() {
    private val testInput = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var interpreter : FirebaseModelInterpreter?
    private var inputOutputOptions :FirebaseModelInputOutputOptions?

    init {
        val localModel = FirebaseCustomLocalModel.Builder()
            .setAssetFilePath("tf/munUrbanModel.tflite")
            .build()
        val options = FirebaseModelInterpreterOptions.Builder(localModel).build()
        interpreter = FirebaseModelInterpreter.getInstance(options)
                inputOutputOptions = FirebaseModelInputOutputOptions.Builder()
            .setInputFormat(0, FirebaseModelDataType.FLOAT32, intArrayOf(1,99))
            .setOutputFormat(0, FirebaseModelDataType.FLOAT32, intArrayOf(1,10))
            .build()
    }
    fun runModel(parameterDTO: ParameterDTO,callback: (result: FloatArray) -> Unit){
        val input = parametersModelInput(parameterDTO.municipalitySortedIndex, parameterDTO.urban)
        val floatInputs = input.map{it.toFloat()}

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
    private fun parametersModelInput(municipalityIndex:Int, urban:Boolean): IntArray {
        var parameters = IntArray(99)
        for (index in 0..parameters.lastIndex){
            //The urban parameter is on 98 and not on 99
            parameters[index] = if(index==municipalityIndex) 1 else if(index==98 && urban) 1 else 0
        }
        return parameters
    }
}