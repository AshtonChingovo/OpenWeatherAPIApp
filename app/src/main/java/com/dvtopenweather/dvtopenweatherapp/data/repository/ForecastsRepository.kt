package com.dvtopenweather.dvtopenweatherapp.data.repository

import com.dvtopenweather.dvtopenweatherapp.data.remote.model.openWeather.OpenWeatherFiveDayForecastResource
import com.dvtopenweather.dvtopenweatherapp.data.repository.model.ForecastEntity
import kotlinx.coroutines.flow.Flow

interface ForecastsRepository {

    fun insertForecasts(forecasts: List<ForecastEntity>)

    fun getForecasts(): Flow<List<ForecastEntity>>

    fun updateForecastIsFavourite(isFavourite: Boolean, id: Int)

}