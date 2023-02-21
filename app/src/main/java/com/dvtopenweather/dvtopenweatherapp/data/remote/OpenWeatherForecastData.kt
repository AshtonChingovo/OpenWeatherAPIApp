package com.dvtopenweather.dvtopenweatherapp.data.remote

import com.dvtopenweather.dvtopenweatherapp.data.remote.model.openWeather.OpenWeatherCurrentWeatherForecastResource
import com.dvtopenweather.dvtopenweatherapp.data.remote.model.openWeather.OpenWeatherFiveDayForecastResource

interface OpenWeatherForecastData {

    suspend fun fetchCurrentWeatherForecast(
        latitude: Double,
        longitude: Double,
        apiKey: String
    ): OpenWeatherCurrentWeatherForecastResource?

    suspend fun fiveDayWeatherForecast(
        latitude: String,
        longitude: String,
        apiKey: String
    ): OpenWeatherFiveDayForecastResource?

}