package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.NasaApiService
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.database.PictureOfDayDao
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.PictureOfDay
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
     */
    suspend fun refresh() {
        withContext(Dispatchers.IO) {
            try {
                val networkPictureOfDay = nasaService.getPictureOfTheDay()
                if (networkPictureOfDay.mediaType == "image") {
                    dao.insertPictureOfDay(networkPictureOfDay.asDatabaseModel())
                } else {
                    Log.i(
                        "PictureOfDayRepository",
                        "Picture of day ignored (media type = ${networkPictureOfDay.mediaType})"
                    )
                }
            } catch (e: Exception) {
                Log.e("PictureOfDayRepository", e.toString())
                throw e
            }
        }
    }
}
