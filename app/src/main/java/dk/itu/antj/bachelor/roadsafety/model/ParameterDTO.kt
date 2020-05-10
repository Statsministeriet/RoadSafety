package dk.itu.antj.bachelor.roadsafety.model

import dk.itu.antj.bachelor.roadsafety.Constants

class ParameterDTO(firstName:String, age:Int, municipality:String, speed:Int, urban:Boolean, genderMale:Boolean, temperature:Double, sunUp:Boolean) {
    var firstName:String
    var age:Int
    var municipality:String
    var speed:Int
    var urban:Boolean
    var municipalitySortedIndex:Int
    var genderMale:Boolean
    var temperature:Int
    var sunUp:Boolean

    init {
        this.firstName = firstName
        this.age = age
        this.municipality = municipality
        this.speed = speed
        this.urban = urban
        municipalitySortedIndex = Constants.SORTED_MUNICIPALITIES.indexOf(municipality)
        this.genderMale = genderMale
        this.temperature = kotlin.math.round(temperature).toInt()
        this.sunUp = sunUp
    }
}