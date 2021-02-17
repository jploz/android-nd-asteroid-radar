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
                dao.insertPictureOfDay(networkPictureOfDay.asDatabaseModel())
            } catch (e: Exception) {
                Log.e("PictureOfDayRepository", e.toString())
                throw e
            }
        }
    }
}
