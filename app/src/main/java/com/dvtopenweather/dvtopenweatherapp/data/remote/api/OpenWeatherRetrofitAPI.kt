package com.dvtopenweather.dvtopenweatherapp.data.remote.api

import com.dvtopenweather.dvtopenweatherapp.data.remote.model.openWeather.OpenWeatherCurrentWeatherForecastResource
import com.dvtopenweather.dvtopenweatherapp.data.remote.model.openWeather.OpenWeatherFiveDayForecastResource
import com.dvtopenweather.dvtopenweatherapp.util.OPEN_WEATHER_DATA_UNITS
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherRetrofitAPI {

    @GET("weather")
    suspend fun currentWeatherForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        // ensures the api returned temperature is in degrees
        @Query("units") units: String = OPEN_WEATHER_DATA_UNITS,
    ): OpenWeatherCurrentWeatherForecastResource

    @GET("forecast")
    suspend fun fiveDayWeatherForecast(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): OpenWeatherFiveDayForecastResource

}