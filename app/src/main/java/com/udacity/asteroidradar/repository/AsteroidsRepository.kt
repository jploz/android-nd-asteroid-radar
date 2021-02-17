package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.api.NasaNeowsApi
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidRadarDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(
    private val database: AsteroidRadarDatabase,
    private val networkApi: NasaNeowsApi
) {

    /**
     * A list of asteroids (domain models) that can be shown on the screen.
     */
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    /**
     * Refresh the list of asteroids stored in the offline cache.
     */
    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val asteroidsResponse = networkApi.service.getAsteroids()
                val networkAsteroids = parseAsteroidsJsonResult(JSONObject(asteroidsResponse))
                database.asteroidDao.insertAll(*networkAsteroids.asDatabaseModel())
            } catch (e: Exception) {
                Log.e("AsteroidsRepository", e.toString())
                throw e
            }
        }
    }
}
