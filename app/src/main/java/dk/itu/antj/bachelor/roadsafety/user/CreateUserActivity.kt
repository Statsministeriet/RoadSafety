package dk.itu.antj.bachelor.roadsafety.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dk.itu.antj.bachelor.roadsafety.R


class CreateUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
