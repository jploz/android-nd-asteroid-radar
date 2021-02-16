package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Constants
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun parseAsteroidsJsonResult(jsonResult: JSONObject): List<NetworkAsteroid> {
    val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")

    val asteroidList = ArrayList<NetworkAsteroid>()

    val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
    for (formattedDate in nextSevenDaysFormattedDates) {
        val dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(formattedDate)

        val parseDateAsteroidJsonArray = parseDateAsteroidJsonArray(
            formattedDate,
            dateAsteroidJsonArray
        )
        asteroidList.addAll(parseDateAsteroidJsonArray)
    }

    return asteroidList
}

fun parseDateAsteroidJsonArray(
    formattedDate: String,
    dateAsteroidJsonArray: JSONArray
): ArrayList<NetworkAsteroid> {
    val asteroidList = ArrayList<NetworkAsteroid>()

    for (i in 0 until dateAsteroidJsonArray.length()) {
        val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
        val asteroid = parseDateAsteroidJson(formattedDate, asteroidJson)
        asteroidList.add(asteroid)
    }
    return asteroidList
}

fun parseDateAsteroidJson(
    formattedDate: String,
    asteroidJson: JSONObject
): NetworkAsteroid {
    val id = asteroidJson.getLong("id")
    val codename = asteroidJson.getString("name")
    val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")

    val estimatedDiameter = asteroidJson
        .getJSONObject("estimated_diameter")
        .getJSONObject("kilometers")
        .getDouble("estimated_diameter_max")

    val closeApproachData = asteroidJson
        .getJSONArray("close_approach_data")
        .getJSONObject(0)

    val relativeVelocity = closeApproachData
        .getJSONObject("relative_velocity")
        .getDouble("kilometers_per_second")

    val distanceFromEarth = closeApproachData
        .getJSONObject("miss_distance")
        .getDouble("astronomical")

    val isPotentiallyHazardous = asteroidJson
        .getBoolean("is_potentially_hazardous_asteroid")

    return NetworkAsteroid(
        id,
        codename,
        formattedDate,
        absoluteMagnitude,
        estimatedDiameter,
        relativeVelocity,
        distanceFromEarth,
        isPotentiallyHazardous
    )
}

fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()

    val calendar = Calendar.getInstance()
    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        formattedDateList.add(dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return formattedDateList
}
