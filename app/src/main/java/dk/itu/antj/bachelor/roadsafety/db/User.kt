package dk.itu.antj.bachelor.roadsafety.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "birth_year") val birthYear: Int,
    @ColumnInfo(name = "birth_month") val birthMonth: Int,
    @ColumnInfo(name = "birth_day") val birthDay: Int,
    @ColumnInfo(name = "male") val male: Boolean
    )