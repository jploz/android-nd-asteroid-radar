package com.udacity.asteroidradar

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


fun getTodayDateFormatted(): String {
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    val calendar = Calendar.getInstance()
    return dateFormat.format(calendar.time)
}

fun getOffsetDateFormatted(date: String, offsetInDays: Int): String {
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.time = dateFormat.parse(date)
    calendar.add(Calendar.DAY_OF_YEAR, offsetInDays)
    return dateFormat.format(calendar.time)
}

fun getNextDaysFormattedDates(numberOfDays: Int): ArrayList<String> {
    val formattedDateList = ArrayList<String>()

    val calendar = Calendar.getInstance()
    for (i in 0..numberOfDays) {
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        formattedDateList.add(dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return formattedDateList
}
