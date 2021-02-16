package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.NasaNeowsApi
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }

    private val database = AsteroidsDatabase.getDatabase(application)
    private val networkApi = NasaNeowsApi
    private val asteroidsRepository = AsteroidsRepository(database, networkApi)

    val asteroids = asteroidsRepository.asteroids

    /**
     * Call refreshAsteroids() on init so we can display status immediately.
     */
    init {
        refreshAsteroids()
    }

    /**
     * Sets the value of the status LiveData to the NeoWs API status.
     */
    private fun refreshAsteroids() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                asteroidsRepository.refreshAsteroids()
            } catch (e: Exception) {
                Log.e("MainViewModel", "${e}")
            }
        }
    }
}
