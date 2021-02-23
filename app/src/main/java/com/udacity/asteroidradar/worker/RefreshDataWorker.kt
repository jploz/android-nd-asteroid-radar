package com.udacity.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.database.AsteroidRadarDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.repository.PictureOfDayRepository
import retrofit2.HttpException


class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "AsteroidRadarRefreshDataWorker"
    }

    /**
     * List of asteroids and latest picture of the day is updated.
     */
    override suspend fun doWork(): Result {
        val database = AsteroidRadarDatabase.getDatabase(applicationContext)
        val asteroidsRepository = AsteroidsRepository(
            database.asteroidDao,
            NasaApi.service
        )
        val pictureOfDayRepository = PictureOfDayRepository(
            database.pictureOfDayDao,
            NasaApi.service
        )

        return try {
            asteroidsRepository.refreshAsteroids()
            pictureOfDayRepository.refresh()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}
