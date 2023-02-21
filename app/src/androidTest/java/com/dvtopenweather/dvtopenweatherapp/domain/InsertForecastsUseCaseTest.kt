package com.dvtopenweather.dvtopenweatherapp.domain

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.dvtopenweather.dvtopenweatherapp.data.remote.model.openWeather.*
import com.dvtopenweather.dvtopenweatherapp.data.repository.ForecastsRepository
import com.dvtopenweather.dvtopenweatherapp.data.repository.ForecastsRepositoryImpl
import com.dvtopenweather.dvtopenweatherapp.data.repository.LocationsRepository
import com.dvtopenweather.dvtopenweatherapp.data.repository.dao.ForecastDao
import com.dvtopenweather.dvtopenweatherapp.data.repository.dao.LocationDao
import com.dvtopenweather.dvtopenweatherapp.data.repository.database.DvtOpenWeatherDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class InsertForecastsUseCaseTest{

    lateinit var dvtOpenWeatherDatabase: DvtOpenWeatherDatabase
    lateinit var forecastDao: ForecastDao
    lateinit var forecastsRepository: ForecastsRepository
    lateinit var insertForecastsUseCase: InsertForecastsUseCase

    @Before
    fun initDB(){

        dvtOpenWeatherDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DvtOpenWeatherDatabase::class.java)
            .build()

        forecastDao = dvtOpenWeatherDatabase.forecastDao()
        forecastsRepository = ForecastsRepositoryImpl(forecastDao)
        insertForecastsUseCase = InsertForecastsUseCase(forecastsRepository)

    }

    @After
    fun closeDb() {
        dvtOpenWeatherDatabase.close()
    }

    @Test
    fun insert_forecasts_use_case_inserts_forecast_into_db() = runTest {

        var fiveDayForecastResource = getOpenWeatherFiveDayForecastResource()

        // insert two forecast objects into db
        insertForecastsUseCase(fiveDayForecastResource)

        var flowResultsSize = forecastsRepository.getForecasts().first().toList().size

        assertEquals(2, flowResultsSize)
    }

    private fun getOpenWeatherFiveDayForecastResource(): OpenWeatherFiveDayForecastResource{

        var openWeatherDailyForecastResource = OpenWeatherDailyForecastResource(
            unixTimeStamp = 10L,
            temperatureDetailsResource = OpenWeatherTemperatureWeatherDetailsResource(23.0, 88.0, 8.8),
            weatherDetailsResource = listOf(OpenWeatherWeatherResource("Rainy")),
            date = "",
        )

        var forecasts = listOf(openWeatherDailyForecastResource, openWeatherDailyForecastResource)

        var city = OpenWeatherCityResource(
            city = "XYZ",
            country = "ABC",
            coordinates = UserCoordinates(-17.234, 43.3534)
        )

        return OpenWeatherFiveDayForecastResource(
            forecast = forecasts,
            city = city
        )

    }

}