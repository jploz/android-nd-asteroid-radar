package com.udacity.asteroidradar

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


fun getTodayDateFormatted(): String {
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    val calendar = Calendar.getInstance()
    return dateFormat.format(calendar.time)
}

fun getNextWeekEndDateFormatted(today: String): String {
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.time = dateFormat.parse(today)
    calendar.add(Calendar.DAY_OF_YEAR, 7)
    return dateFormat.format(calendar.time)
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
