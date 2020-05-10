package dk.itu.antj.bachelor.roadsafety.user

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Toast
import dk.itu.antj.bachelor.roadsafety.Constants
import dk.itu.antj.bachelor.roadsafety.R
import dk.itu.antj.bachelor.roadsafety.db.AppDatabase
import dk.itu.antj.bachelor.roadsafety.db.User

import kotlinx.android.synthetic.main.activity_list_users.*
import kotlinx.android.synthetic.main.content_list_users.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ListUsers : AppCompatActivity(), CoroutineScope {
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_users)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        start_create_user_button.setOnClickListener {
            val intent = Intent(this, CreateUserActivity::class.java)
            startActivity(intent)
        }
        val db = AppDatabase.getInstance(this)

        launch {
            val result = db.userDao().getAll()
            onResult(result)
        }
    }
    private fun onResult(result: List<User>){
        val adapter =
            UserAdapter(this, result)
        user_list_view.adapter = adapter
        user_list_view.setOnItemClickListener { _, _, position, _ ->
            val user = result[position]
            val editor = applicationContext.getSharedPreferences(Constants.FILE_NAME, Context.MODE_PRIVATE).edit()
            editor.putInt(Constants.CURRENT_USER_LOGIN_ID, user.uid)
            editor.putString(Constants.CURRENT_USER_LOGIN_FIRSTNAME, user.firstName)
            editor.putString(Constants.CURRENT_USER_LOGIN_LASTNAME, user.lastName)
            editor.putInt(Constants.CURRENT_USER_LOGIN_BIRTH_YEAR, user.birthYear)
            editor.putInt(Constants.CURRENT_USER_LOGIN_BIRTH_MONTH, user.birthMonth)
            editor.putInt(Constants.CURRENT_USER_LOGIN_BIRTH_DAY, user.birthDay)
            editor.putBoolean(Constants.CURRENT_USER_LOGIN_MALE, user.male)
            editor.apply()
            Toast.makeText(this, user.uid.toString(), Toast.LENGTH_SHORT).show()
        }

    }

}
