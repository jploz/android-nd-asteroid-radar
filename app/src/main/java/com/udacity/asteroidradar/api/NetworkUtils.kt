package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.getNextDaysFormattedDates
import org.json.JSONArray
import org.json.JSONObject

fun parseAsteroidsJsonResult(jsonResult: JSONObject): List<NetworkAsteroid> {
    val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")

    val asteroidList = ArrayList<NetworkAsteroid>()

    val nextSevenDaysFormattedDates = getNextDaysFormattedDates(Constants.NEO_FEED_DATE_LIMIT)
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
