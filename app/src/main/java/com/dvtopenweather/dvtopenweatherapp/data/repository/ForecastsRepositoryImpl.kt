package com.dvtopenweather.dvtopenweatherapp.data.repository

import com.dvtopenweather.dvtopenweatherapp.data.remote.model.openWeather.OpenWeatherFiveDayForecastResource
import com.dvtopenweather.dvtopenweatherapp.data.repository.dao.ForecastDao
import com.dvtopenweather.dvtopenweatherapp.data.repository.model.ForecastEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ForecastsRepositoryImpl @Inject constructor(
    private var forecastDao: ForecastDao
) : ForecastsRepository {

    override fun insertForecasts(forecasts: List<ForecastEntity>) {
        forecastDao.insertForecasts(forecasts)
    }

    override fun getForecasts(): Flow<List<ForecastEntity>> = forecastDao.getForecasts()

    override fun updateForecastIsFavourite(isFavourite: Boolean, id: Int) {
        forecastDao.updateFavouriteState(isFavourite, id)
    }


}

// parse OpenWeatherResource to local ForecastEntity
private fun parseToForecastEntities(
    fiveDayForecastResource: OpenWeatherFiveDayForecastResource,
): List<ForecastEntity> {
    return fiveDayForecastResource.forecast.map { openWeather ->
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
}