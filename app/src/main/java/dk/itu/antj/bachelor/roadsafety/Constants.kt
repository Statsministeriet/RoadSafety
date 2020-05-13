package dk.itu.antj.bachelor.roadsafety

object Constants {
    const val FILE_NAME ="UserDataRoadSafety"
    const val CURRENT_USER_LOGIN_FIRSTNAME ="UserNameLoggedInRoadSafety"
    const val CURRENT_USER_LOGIN_LASTNAME ="UserLastNameLoggedInRoadSafety"
    const val CURRENT_USER_LOGIN_BIRTH_YEAR ="UserBirthYearLoggedInRoadSafety"
    const val CURRENT_USER_LOGIN_BIRTH_MONTH ="UserBirthMonthLoggedInRoadSafety"
    const val CURRENT_USER_LOGIN_BIRTH_DAY ="UserBirthDayLoggedInRoadSafety"
    const val CURRENT_USER_LOGIN_MALE ="UserMaleLoggedInRoadSafety"
    const val NO_VAL_DEFAULT = "NoValue"
    const val CURRENT_USER_LOGIN_ID = "UserIdLoggedInRoadSafety"

    val SORTED_MUNICIPALITIES = arrayOf("Aabenraa","Aalborg","Aarhus","Ærø","Albertslund","Allerød","Assens","Ballerup","Billund","Bornholm","Brøndby","Brønderslev","Dragør","Egedal","Esbjerg","Faaborg-Midtfyn","Fanø","Favrskov","Faxe","Fredensborg","Fredericia","Frederiksberg","Frederikshavn","Frederikssund","Furesø","Gentofte","Gladsaxe","Glostrup","Greve","Gribskov","Guldborgsund","Haderslev","Halsnæs","Hedensted","Helsingør","Herlev","Herning","Hillerød","Hjørring","Høje-Taastrup","Holbæk","Holstebro","Horsens","Hørsholm","Hvidovre","Ikast-Brande","Ishøj","Jammerbugt","Kalundborg","Kerteminde","København","Køge","Kolding","Læsø","Langeland","Lejre","Lemvig","Lolland","Lyngby-Taarbæk","Mariagerfjord","Middelfart","Morsø","Næstved","Norddjurs","Nordfyn","Nyborg","Odder","Odense","Odsherred","Randers","Rebild","Ringkøbing-Skjern","Ringsted","Rødovre","Roskilde","Rudersdal","Samsø","Silkeborg","Skanderborg","Skive","Slagelse","Solrød","Sønderborg","Sorø","Stevns","Struer","Svendborg","Syddjurs","Tårnby","Thisted","Tønder","Vallensbæk","Varde","Vejen","Vejle","Vesthimmerland","Viborg",
        "Vordingborg")
    val ACCIDENT_TYPES_DANISH = arrayOf("Eneuheld",
    "Indhentningsuheld",
    "Mødeuheld",
    "Svingningsuheld mellem medkørende",
    "Svingningsuheld mellem modkørende",
    "Uheld mellem krydsende køretøjer",
    "Svingningsuheld mellem krydsende køretøjer",
    "Parkeringsuheld",
    "Fodgængeruheld",
    "Forhindringsuheld")

    val ACCIDENT_TYPES = arrayOf(
        "one-vehicle accidents",
        "vehicles on same road going in same direction without turning from road",
        "vehicles on same road going in opposite directions without turning from road",
        "vehicles on same road going in same direction, turning into T-junction, Y-junction, crossroads",
        "vehicles on same road going in opposite direction, turning into T-junction, Y-junction,crossroads",
        "vehicles on different roads meeting in crossroads without turning",
        "vehicles on different roads meeting in T-junction, Y-junction, crossroads, etc. turning",
        "accidents involving parked vehicles",
        "accidents involving pedestrians",
        "accident involving animals, obstacles, etc., on or above roadway")

    //Math confidence intervals
    val Z_VALUE_90 = 1.645
    val Z_VALUE_95 = 1.96
    val Z_VALUE_99 = 2.576

    val SELECTED_Z_VALUES = "SELECTED_Z_VALUES"
    val Z_VALUE_INTERPRETER = "Z_VALUE_INTERPRETER"

    val FREQUENCY_SHORT = 1000.toLong()
    val FREQUENCY_MEDIUM = 1500.toLong()
    val FREQUENCY_LONG = 2000.toLong()

    val SELECTED_FREQUENCY = "SELECTED_FREQUENCY"
    val SELECTED_FREQUENCY_INTERPRETER = "SELECTED_FREQUENCY_INTERPRETER"


    val INIT_EVALUATIONS = 20

    val AUTO_DETECTION = "AUTO_DETECTION"


}
