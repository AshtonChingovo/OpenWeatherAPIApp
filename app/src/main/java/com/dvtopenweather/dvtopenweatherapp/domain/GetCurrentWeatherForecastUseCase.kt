package com.dvtopenweather.dvtopenweatherapp.domain

import com.dvtopenweather.dvtopenweatherapp.data.remote.OpenWeatherForecastData
import com.dvtopenweather.dvtopenweatherapp.data.repository.model.ForecastEntity
import com.dvtopenweather.dvtopenweatherapp.di.APIKeysModule
import javax.inject.Inject

class GetCurrentWeatherForecastUseCase @Inject constructor(
    var openWeatherForecastData: OpenWeatherForecastData,
    @APIKeysModule.OpenWeatherAPIKEY var openWeatherAPIKEY: String
){

    suspend operator fun invoke(latitude: Double, longitude: Double): ForecastEntity{

        var currentForecast = openWeatherForecastData.fetchCurrentWeatherForecast(latitude, longitude, openWeatherAPIKEY)

        return ForecastEntity(
            id = 0,
            forecastDate = "",
            weather = currentForecast.weather[0].weather,
            weatherForecastTime = "",
            minimumTemperature = currentForecast.temperature.minTemperature,
            currentTemperature = currentForecast.temperature.currentTemperature,
            maximumTemperature = currentForecast.temperature.maxTemperature,
            humidity = currentForecast.temperature.humidity,
            city = currentForecast.city,
            country = currentForecast.country.country,
            latitude = 0.0,
            longitude = 0.0,
            isFavourite = false
        )

    }

}