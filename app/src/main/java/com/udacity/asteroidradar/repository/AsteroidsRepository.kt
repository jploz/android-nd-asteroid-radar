package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.NasaApiService
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.getNextWeekEndDateFormatted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(
    private val dao: AsteroidDao,
    private val nasaService: NasaApiService
) {

    /**
     * List of all asteroids (domain models) for displaying.
     */
    fun getAsteroidsAll(): LiveData<List<Asteroid>> =
        Transformations.map(
            dao.getAsteroids()
        ) { it.asDomainModel() }

    /**
     * List of next weeks asteroids (domain models) for displaying.
     */
    fun getAsteroidsNextWeek(fromDate: String): LiveData<List<Asteroid>> =
        Transformations.map(
            dao.getAsteroidsByCloseApproachDate(
                fromDate = fromDate,
                toDate = getNextWeekEndDateFormatted(fromDate)
            )
        ) { it.asDomainModel() }

    /**
     * List of todays asteroids (domain models) for displaying.
     */
    fun getAsteroidsToday(today: String): LiveData<List<Asteroid>> =
        Transformations.map(
            dao.getAsteroidsByCloseApproachDate(
                fromDate = today,
                toDate = today
            )
        ) { it.asDomainModel() }

    /**
     * Refresh the list of asteroids stored in the offline cache.
     */
    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val asteroidsResponse = nasaService.getAsteroids()
                val networkAsteroids = parseAsteroidsJsonResult(JSONObject(asteroidsResponse))
                dao.insertAll(*networkAsteroids.asDatabaseModel())
            } catch (e: Exception) {
                Log.e("AsteroidsRepository", e.toString())
                throw e
            }
        }
    }
}
