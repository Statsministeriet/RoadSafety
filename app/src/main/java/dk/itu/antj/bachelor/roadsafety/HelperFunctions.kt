package dk.itu.antj.bachelor.roadsafety

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient

class HelperFunctions {

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
/**
    fun getLocations(fusedLocationClient:FusedLocationProviderClient, applicationContext: Context){
        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
            Toast.makeText(applicationContext, Pair(location?.latitude, location?.longitude).toString(), Toast.LENGTH_LONG).show()
            Pair(location?.latitude, location?.longitude)
        }
    }

    fun getLocations(fusedLocationClient:FusedLocationProviderClient, applicationContext:Context):String{
        var end = ""
        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
            Toast.makeText(applicationContext, location?.speed.toString(), Toast.LENGTH_LONG).show()
            val speed = location?.speed
            end = when{
                speed == null -> "fail"
                speed <= 20.toFloat() ->    "20"
                speed <= 50.toFloat() ->    "20"
                speed <= 60.toFloat() ->    "20"
                speed <= 70.toFloat() ->    "20"
                speed <= 80.toFloat() ->    "20"
                speed <= 90.toFloat() ->    "20"
                speed <= 100.toFloat() ->   "20"
                speed <= 110.toFloat() ->   "20"
                speed <= 120.toFloat() ->   "20"
                speed > 120.toFloat() ->    "20"

                else -> "OUT OF RANGE"
            }
        }
        return end;
    }*/
}