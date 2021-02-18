package com.udacity.asteroidradar.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.database.AsteroidRadarDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.repository.PictureOfDayRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val asteroidsRepository: AsteroidsRepository
    private val pictureOfDayRepository: PictureOfDayRepository

    val asteroids: LiveData<List<Asteroid>>
    val pictureOfTheDay: LiveData<PictureOfDay>

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()
    val navigateToSelectedAsteroid: LiveData<Asteroid?> = _navigateToSelectedAsteroid

    /**
     * Call refreshAsteroids() on init so we can display status immediately.
     */
    init {
        val database = AsteroidRadarDatabase.getDatabase(application)

        asteroidsRepository = AsteroidsRepository(
            database.asteroidDao,
            NasaApi.service
        )
        pictureOfDayRepository = PictureOfDayRepository(
            database.pictureOfDayDao,
            NasaApi.service
        )

        asteroids = asteroidsRepository.asteroids
        pictureOfTheDay = pictureOfDayRepository.pictureOfDay

        refreshAsteroids()
        refreshPictureOfTheDay()
    }

    fun navigateToAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun doneNavigateToAsteroidDetails() {
        _navigateToSelectedAsteroid.value = null
    }

    /**
     * Sets the value of the status LiveData to the NeoWs API status.
     */
    private fun refreshAsteroids() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                asteroidsRepository.refreshAsteroids()
            } catch (e: Exception) {
                Log.e("MainViewModel", "$e")
            }
        }
    }

    private fun refreshPictureOfTheDay() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                pictureOfDayRepository.refresh()
            } catch (e: Exception) {
                Log.e("MainViewModel", "$e")
            }
        }
    }
}
