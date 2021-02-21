package com.udacity.asteroidradar.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.database.AsteroidRadarDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.getTodayDateFormatted
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.repository.PictureOfDayRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Status of selected UI mode: show all, show weekly, show todays
 */
enum class AsteroidsUiFilter {
    SHOW_ASTEROIDS_ALL,
    SHOW_ASTEROIDS_TODAYS,
    SHOW_ASTEROIDS_WEEKS
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val asteroidsRepository: AsteroidsRepository
    private val pictureOfDayRepository: PictureOfDayRepository

    val asteroids: LiveData<List<Asteroid>>
    val pictureOfTheDay: LiveData<PictureOfDay>

    private val asteroidsUiFilter = MutableLiveData(AsteroidsUiFilter.SHOW_ASTEROIDS_WEEKS)

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

        asteroids = Transformations.switchMap(asteroidsUiFilter) {
            when (it!!) {
                AsteroidsUiFilter.SHOW_ASTEROIDS_ALL -> asteroidsRepository.getAsteroidsAll()
                AsteroidsUiFilter.SHOW_ASTEROIDS_TODAYS -> asteroidsRepository.getAsteroidsToday(
                    getTodayDateFormatted()
                )
                AsteroidsUiFilter.SHOW_ASTEROIDS_WEEKS -> asteroidsRepository.getAsteroidsNextWeek(
                    getTodayDateFormatted()
                )
            }
        }

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
