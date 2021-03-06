package com.udacity.asteroidradar

import org.junit.Assert.assertEquals
import org.junit.Test


class UtilsKtTest {

    @Test
    fun getNextSevenDaysFormattedDates_correctNumberOfDates() {
        val nextSevenDaysFormattedDates = getNextDaysFormattedDates(Constants.NEO_FEED_DATE_LIMIT)
        // should return list of dates like ["2020-12-02","2020-12-03",...]
        // list should contain 8 dates: today plus next 7 days
        assertEquals(8, nextSevenDaysFormattedDates.size)
    }

    @Test
    fun getNextWeekEndDateFormatted_correctEndDate() {
        val start = "2020-12-02"
        val end = getOffsetDateFormatted(start, Constants.DAYS_IN_WEEK)
        assertEquals("2020-12-09", end)
    }

}
