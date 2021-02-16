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

enum class NasaNeowsApiStatus { LOADING, ERROR, DONE }

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
 * A retrofit service to fetch a list of asteroids.
 */
interface NasaNeowsApiService {
    @GET("neo/rest/v1/feed?api_key=DEMO_KEY")
    suspend fun getAsteroids(): String
}

object NasaNeowsApi {
    val service = retrofit.create(NasaNeowsApiService::class.java)
}
