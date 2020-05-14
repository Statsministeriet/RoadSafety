package dk.itu.antj.bachelor.roadsafety


import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dk.itu.antj.bachelor.roadsafety.model.MainModel
import dk.itu.antj.bachelor.roadsafety.model.ModelException
import dk.itu.antj.bachelor.roadsafety.user.ListUsers
import kotlinx.android.synthetic.main.activity_main.*
class MainActivity() : AppCompatActivity() {
    var running = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permissionHandler(applicationContext,this, Manifest.permission.ACCESS_FINE_LOCATION, 100)
        permissionHandler(applicationContext,this, Manifest.permission.INTERNET, 110)
        initUserData()
        this.createNotificationChannel()
        start_model.setBackgroundColor(Color.parseColor("#CCE5CC"))
        //Main Model Treated like a singleton
        val mainModel = MainModel(this)

        start_model.setOnClickListener {
            running = !running
            if(running) {
                try {
                    mainModel.onStart(applicationContext)

                }catch (e:ModelException){
                    Toast.makeText(this,e.message,Toast.LENGTH_LONG)
                }
                start_model.text = "Stop model"
                start_model.setBackgroundColor(Color.parseColor("#F0B2B2"))
                uiLock(false)
            }else {
                mainModel.onStop()
                start_model.setBackgroundColor(Color.parseColor("#CCE5CC"))
                start_model.text = "Start model"
                uiLock(true)
            }
        }
        val sharedPref = applicationContext.getSharedPreferences(Constants.FILE_NAME, Context.MODE_PRIVATE)

        autoStartToggle.isChecked = sharedPref.getBoolean(Constants.AUTO_DETECTION, true)
        autoStartToggle.setOnCheckedChangeListener{ _, isChecked ->
            val editor = sharedPref.edit()
            editor.putBoolean(Constants.AUTO_DETECTION, isChecked)
            editor.apply()
        }

        setSeekersListener()
    }

    private fun initUserData(){
        val sharedPref = applicationContext.getSharedPreferences(Constants.FILE_NAME, Context.MODE_PRIVATE)
        val userName = sharedPref.getString(Constants.CURRENT_USER_LOGIN_FIRSTNAME, Constants.NO_VAL_DEFAULT)
        if(userName!==Constants.NO_VAL_DEFAULT){
            user_name_landing.text = userName
            start_model.isEnabled = true
            list_users_button.text = "Select different user"

        }else{
            start_model.isEnabled = false
            list_users_button.text = "Select a user"
            Toast.makeText(applicationContext, "To create new user, go to select different user and tab the button in the bottom right corner", Toast.LENGTH_LONG).show()
        }
        //Init location fun
        list_users_button.setOnClickListener {
            val intent = Intent(this, ListUsers::class.java)
            startActivity(intent)
        }
    }

    private fun permissionHandler(applicationContext: Context, activity: Activity, permission:String, requestCode:Int){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(applicationContext,
                permission)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    permission)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Toast.makeText(applicationContext, "The location is needed in order to improve quality of content", Toast.LENGTH_LONG).show()
                ActivityCompat.requestPermissions(activity,
                    arrayOf(permission), requestCode)

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                    arrayOf(permission), requestCode)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    private fun setSeekersListener(){
        val sharedPref = applicationContext.getSharedPreferences(Constants.FILE_NAME, Context.MODE_PRIVATE)

        frequencySeekBar.progress = sharedPref.getInt(Constants.SELECTED_FREQUENCY, 0)
        frequencySeekBar.max = 2
        frequencySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val editor = sharedPref.edit()
                editor.putInt(Constants.SELECTED_FREQUENCY,i)
                val fVal = if(i==0)Constants.FREQUENCY_SHORT else if(i==1)Constants.FREQUENCY_MEDIUM else Constants.FREQUENCY_LONG
                editor.putLong(Constants.SELECTED_FREQUENCY_INTERPRETER, fVal)
                editor.apply()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        sensibilitySeekBar.progress = sharedPref.getInt(Constants.SELECTED_Z_VALUES, 0)
        sensibilitySeekBar.max = 2
        sensibilitySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val editor = sharedPref.edit()
                editor.putInt(Constants.SELECTED_Z_VALUES,i)
                val zVal = if(i==0)Constants.Z_VALUE_90 else if(i==1)Constants.Z_VALUE_95 else Constants.Z_VALUE_99
                editor.putLong(Constants.Z_VALUE_INTERPRETER, zVal.toLong())
                editor.apply()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun uiLock(predicate: Boolean){
        autoStartToggle.isEnabled = predicate
        sensibilitySeekBar.isEnabled = predicate
        frequencySeekBar.isEnabled = predicate
        list_users_button.isEnabled = predicate

    }

    private fun createNotificationChannel() {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(Constants.FILE_NAME, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

    }
}
