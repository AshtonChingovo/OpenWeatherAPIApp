package com.dvtopenweather.dvtopenweatherapp.domain

import com.dvtopenweather.dvtopenweatherapp.data.remote.model.openWeather.OpenWeatherFiveDayForecastResource
import com.dvtopenweather.dvtopenweatherapp.data.repository.ForecastsRepository
import com.dvtopenweather.dvtopenweatherapp.data.repository.model.ForecastEntity
import javax.inject.Inject

class InsertForecastsUseCase @Inject constructor(
    var forecastsRepository: ForecastsRepository
){

    // parse object to local ForecastEntity type
    operator fun invoke(fiveDayForecastResource: OpenWeatherFiveDayForecastResource){

        var forecasts:List<ForecastEntity> = fiveDayForecastResource.forecast.map { openWeather ->
            ForecastEntity(
                id = 0,
                forecastDate = openWeather.date,
                // OpenWeather returns a list i.e get the first object
                weather = openWeather.weatherDetailsResource[0].weather,
                weatherForecastTime = openWeather.date.substring(
                    openWeather.date.indexOf(" ") + 1
                ),
                minimumTemperature = openWeather.temperatureDetailsResource.minTemperature,
                currentTemperature = openWeather.temperatureDetailsResource.currentTemperature,
                maximumTemperature = openWeather.temperatureDetailsResource.maxTemperature,
                city = fiveDayForecastResource.city.city,
                country = fiveDayForecastResource.city.country,
                latitude = fiveDayForecastResource.city.coordinates.latitude,
                longitude = fiveDayForecastResource.city.coordinates.longitude,
                isFavourite = false
            )

        }

        forecastsRepository.insertForecasts(forecasts)

    }

}