package com.dvtopenweather.dvtopenweatherapp.data.repository

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