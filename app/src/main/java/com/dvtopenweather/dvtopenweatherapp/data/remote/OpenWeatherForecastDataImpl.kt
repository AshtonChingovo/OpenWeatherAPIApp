package com.dvtopenweather.dvtopenweatherapp.data.remote

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.dvtopenweather.dvtopenweatherapp.data.remote.api.OpenWeatherRetrofitAPI
import com.dvtopenweather.dvtopenweatherapp.data.remote.model.openWeather.OpenWeatherCurrentWeatherForecastResource
import com.dvtopenweather.dvtopenweatherapp.data.remote.model.openWeather.OpenWeatherDailyForecastResource
import com.dvtopenweather.dvtopenweatherapp.data.remote.model.openWeather.OpenWeatherFiveDayForecastResource
import com.dvtopenweather.dvtopenweatherapp.data.remote.model.openWeather.OpenWeatherTemperatureWeatherDetailsResource
import com.dvtopenweather.dvtopenweatherapp.util.OPEN_WEATHER_DATA_UNITS
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class OpenWeatherForecastDataImpl @Inject constructor(
    var openWeatherForecastAPI: OpenWeatherRetrofitAPI
) : OpenWeatherForecastData {

    override suspend fun fetchCurrentWeatherForecast(
        latitude: Double,
        longitude: Double,
        apiKey: String
    ): OpenWeatherCurrentWeatherForecastResource {
        return openWeatherForecastAPI.currentWeatherForecast(
            latitude = latitude,
            longitude = longitude,
            apiKey
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun fiveDayWeatherForecast(
        latitude: String,
        longitude: String,
        apiKey: String
    ): OpenWeatherFiveDayForecastResource {
        return openWeatherForecastAPI.fiveDayWeatherForecast(
            latitude = latitude,
            longitude = longitude,
            units = OPEN_WEATHER_DATA_UNITS,
            apiKey = apiKey
        )
    }



}
