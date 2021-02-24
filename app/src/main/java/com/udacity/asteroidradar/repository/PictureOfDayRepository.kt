package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.NasaApiService
import com.udacity.asteroidradar.api.NetworkPictureOfDay
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
     *
     * In case of network errors (e.g. some cases return 404 for a given date if not yet
     * a picture is available), it also tries to fetch an older picture.
     */
    suspend fun refresh() {
        withContext(Dispatchers.IO) {
            val today = getTodayDateFormatted()

            for (offset in 0 downTo -1 * Constants.NUMBER_OF_RETRIES_APOD) {
                val date = getOffsetDateFormatted(date = today, offsetInDays = offset)
                try {
                    val networkPictureOfDay = nasaService.getPictureOfTheDay(date)
                    val success = maybeInsertIntoDatabase(networkPictureOfDay)
                    if (success) {
                        break
                    }
                } catch (e: retrofit2.HttpException) {
                    Log.e("PictureOfDayRepository", "Network error: $e")
                }
            }
        }
    }

    private suspend fun maybeInsertIntoDatabase(networkPictureOfDay: NetworkPictureOfDay): Boolean {
        if (networkPictureOfDay.mediaType == "image") {
            dao.insertPictureOfDay(networkPictureOfDay.asDatabaseModel())
            Log.i(
                "PictureOfDayRepository",
                "Picture of day stored (${networkPictureOfDay})."
            )
            return true
        } else {
            Log.i(
                "PictureOfDayRepository",
                "Picture of day ignored (media type = ${networkPictureOfDay.mediaType})."
            )
        }
        return false
    }
}
