package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface AsteroidDao {

    @Query(
        "select * from databaseasteroid " +
                "ORDER BY closeapproachdate ASC"
    )
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query(
        "SELECT * FROM databaseasteroid " +
                "WHERE closeapproachdate >= :fromDate AND closeapproachdate <= :toDate " +
                "ORDER BY closeapproachdate ASC"
    )
    fun getAsteroidsByCloseApproachDate(
        fromDate: String,
        toDate: String
    ): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg videos: DatabaseAsteroid)
}

@Dao
interface PictureOfDayDao {
    @Query(
        "SELECT * FROM databasepictureofday " +
                "WHERE mediatype = 'image' " +
                "ORDER BY date ASC " +
                "LIMIT 1"
    )
    fun getPictureOfDay(): LiveData<DatabasePictureOfDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPictureOfDay(pictureOfDay: DatabasePictureOfDay)
}
