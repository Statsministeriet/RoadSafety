package dk.itu.antj.bachelor.roadsafety.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.itu.antj.bachelor.roadsafety.db.AppDatabase
import dk.itu.antj.bachelor.roadsafety.db.User
import kotlinx.coroutines.launch
import java.util.*

class CreateUserViewModel : ViewModel() {

    fun createNewUser(firstName:String, lastName:String,birthYear:Int, birthMonth:Int, birthDay:Int, genderFemale:Boolean, genderMale:Boolean, context: Context?, bundle: Bundle?){
        if(firstName.length<3){
            throw UserException("First name to short")
        }else if(lastName.length<3){
            throw UserException("Last name to short")
        }else if(genderFemale==genderMale){
            throw UserException("Gender is incorrect")
        }else if(birthYear<0||birthMonth<0||birthDay<0 || calculateUserAge(birthYear,birthMonth,birthDay)<18){
            throw UserException("Birthday is incorrect")
        }else if(context==null){
            throw UserException("Context is null")
        }else{
            viewModelScope.launch {

                val userDao = AppDatabase.getInstance(context).userDao()
                var uId = userDao.getMaxId()
                if(uId == null){
                    uId = 0
                }
                userDao.insertAll(User(
                    firstName = firstName,
                    lastName = lastName,
                    birthDay = birthDay,
                    birthMonth = birthMonth,
                    birthYear = birthYear,
                    male = genderMale,
                    uid = uId+1
                ))
                val intent = Intent(context, ListUsers::class.java)
                startActivity(context, intent, bundle)
            }
        }
    }
    private fun calculateUserAge(_year:Int, _month:Int, _day:Int):Int{
        val today = Calendar.getInstance()
        val month = if(today.get(Calendar.DAY_OF_MONTH)>=_day) _month+1 else _month
        val year = if(today.get(Calendar.MONTH)>=month)  _year+1 else _year
        return today.get(Calendar.YEAR)-year
    }
}
