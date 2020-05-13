package dk.itu.antj.bachelor.roadsafety

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import dk.itu.antj.bachelor.roadsafety.model.MainModel
import dk.itu.antj.bachelor.roadsafety.model.ParameterDTO

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RoadSafetyInstrumentedTest {

    //This test do return a notification since it runs 20 low risk predictions and number 21 is a high risk notification
    @Test
    fun test_device_receive_notification() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val mainModel = MainModel(appContext)
        val safeSituation = ParameterDTO("Test person",50, "Assens",80,false,true,12.0,true)
        val unsafeSituation = ParameterDTO("Test person",20, "Assens",80,false,true,23.0,true)
        var numberOfPredictions = 0
        for (x in 0..19){
            //Runs 20 low risk predictions
            mainModel.runPredictions(safeSituation, appContext, Constants.Z_VALUE_95)
            numberOfPredictions++
        }
        mainModel.runPredictions(unsafeSituation, appContext, Constants.Z_VALUE_95)
        numberOfPredictions++
        assertEquals(21,numberOfPredictions)
    }

    //This test do not return any notification since it only runs 20 evaluations and the model needs 21 evaluations before sending notifications
    @Test
    fun test_device_do_not_receive_notification() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val mainModel = MainModel(appContext)
        val safeSituation = ParameterDTO("Test person",50, "Assens",80,false,true,12.0,true)
        val unsafeSituation = ParameterDTO("Test person",20, "Assens",80,false,true,23.0,true)
        var numberOfPredictions = 0
        for (x in 0..18){
            //Runs only 19 low risk predictions
            mainModel.runPredictions(safeSituation, appContext, Constants.Z_VALUE_95)
            numberOfPredictions++
        }
        mainModel.runPredictions(unsafeSituation, appContext, Constants.Z_VALUE_95)
        numberOfPredictions++
        assertEquals(20,numberOfPredictions)
    }

    //Runs the test on 100 low risk situations without any notifications.
    //Afterwards the model evaluate the high risk situation and sends a notification and waits for 10 seconds in order to simulate a real situation
    //and after 10 seconds the model evaluates the high risk situation again and do not send any notifications.
    @Test
    fun test_device_do_not_receive_multiple_notification() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val mainModel = MainModel(appContext)
        val safeSituation = ParameterDTO("Test person",50, "Assens",80,false,true,12.0,true)
        val unsafeSituation = ParameterDTO("Test person",20, "Assens",80,false,true,23.0,true)
        var numberOfPredictions = 0
        for (x in 0..99){
            mainModel.runPredictions(safeSituation, appContext, Constants.Z_VALUE_95)
            numberOfPredictions++
        }
        mainModel.runPredictions(unsafeSituation, appContext, Constants.Z_VALUE_95)
        numberOfPredictions++
        Thread.sleep(10000)
        mainModel.runPredictions(unsafeSituation, appContext, Constants.Z_VALUE_95)
        numberOfPredictions++
        assertEquals(102,numberOfPredictions)
    }
}
