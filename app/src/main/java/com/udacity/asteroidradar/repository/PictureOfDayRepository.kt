package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.NasaApiService
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.database.PictureOfDayDao
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.getOffsetDateFormatted
import com.udacity.asteroidradar.getTodayDateFormatted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PictureOfDayRepository(
    private val dao: PictureOfDayDao,
    private val nasaService: NasaApiService
) {

    val pictureOfDay: LiveData<PictureOfDay> =
        Transformations.map(dao.getPictureOfDay()) {
            it?.asDomainModel()
        }

    /**
     * Refresh the picture of the day stored in the offline cache.
     * It will load a picture of one of the previous days in case
     * today's PoD is actually a video.
     */
    suspend fun refresh() {
        withContext(Dispatchers.IO) {
            try {
                val today = getTodayDateFormatted()

                for (offset in 0 downTo -1 * Constants.NUMBER_OF_RETRIES_APOD) {

                    val date = getOffsetDateFormatted(date = today, offsetInDays = offset)
                    val networkPictureOfDay = nasaService.getPictureOfTheDay(date)

                    if (networkPictureOfDay.mediaType == "image") {
                        dao.insertPictureOfDay(networkPictureOfDay.asDatabaseModel())
                        break
                    } else {
                        Log.i(
                            "PictureOfDayRepository",
                            "Picture of day ignored (media type = ${networkPictureOfDay.mediaType})."
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("PictureOfDayRepository", e.toString())
                throw e
            }
        }
    }
}
