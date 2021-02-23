package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//enum class NasaApiStatus { LOADING, ERROR, DONE }

// setup network logging
val interceptor = run {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.apply {
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
    }
}
val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(Constants.BASE_URL)
    .client(client)
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

/**
 * A retrofit service to fetch data from NASA web service APIs
 */
interface NasaApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("api_key") apiKey: String = Constants.NASA_API_KEY
    ): String

    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(
        @Query("date") date: String,
        @Query("api_key") apiKey: String = Constants.NASA_API_KEY
    ): NetworkPictureOfDay
}

object NasaApi {
    val service: NasaApiService = retrofit.create(NasaApiService::class.java)
}
