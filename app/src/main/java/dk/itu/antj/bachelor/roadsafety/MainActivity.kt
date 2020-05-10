package dk.itu.antj.bachelor.roadsafety

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import dk.itu.antj.bachelor.roadsafety.model.MainModel
import dk.itu.antj.bachelor.roadsafety.user.ListUsers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permissionHandler(applicationContext,this)
        initUserData()
        start_model.setOnClickListener {
            MainModel(applicationContext).runModel(applicationContext)
        }
    }

    private fun initUserData(){
        val sharedPref = applicationContext.getSharedPreferences(Constants.FILE_NAME, Context.MODE_PRIVATE)
        val userName = sharedPref.getString(Constants.CURRENT_USER_LOGIN_FIRSTNAME, Constants.NO_VAL_DEFAULT)
        if(userName!==Constants.NO_VAL_DEFAULT){
            user_name_landing.text = userName
        }else{
            Toast.makeText(applicationContext, "To create new user, go to select different user and tab the button in the bottom right corner", Toast.LENGTH_LONG).show()
        }
        //Init location fun
        list_users_button.setOnClickListener {
            val intent = Intent(this, ListUsers::class.java)
            startActivity(intent)
        }
    }

    fun permissionHandler(applicationContext: Context, activity: Activity){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Toast.makeText(applicationContext, "The location is needed in order to improve quality of content", Toast.LENGTH_LONG).show()
                ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }
}
