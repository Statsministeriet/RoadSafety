package dk.itu.antj.bachelor.roadsafety

import android.app.UiModeManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dk.itu.antj.bachelor.roadsafety.model.MainModel
import kotlinx.android.synthetic.main.activity_main.*

class CarModeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        val mainModel = context?.let { MainModel(it) }
        if(UiModeManager.ACTION_ENTER_CAR_MODE == action){
            val sharedPref = context?.getSharedPreferences(Constants.FILE_NAME, Context.MODE_PRIVATE)
            if(sharedPref?.getBoolean(Constants.AUTO_DETECTION, true)!!){
                mainModel?.onStart(context)
            }
        }else if (UiModeManager.ACTION_EXIT_CAR_MODE == action){
            mainModel?.onStop()
        }
    }
}