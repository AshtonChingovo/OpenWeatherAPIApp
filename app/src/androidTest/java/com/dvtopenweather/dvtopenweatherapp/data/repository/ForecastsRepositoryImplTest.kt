package com.dvtopenweather.dvtopenweatherapp.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.dvtopenweather.dvtopenweatherapp.data.repository.dao.ForecastDao
import com.dvtopenweather.dvtopenweatherapp.data.repository.database.DvtOpenWeatherDatabase
import com.dvtopenweather.dvtopenweatherapp.data.repository.model.ForecastEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

internal class ForecastsRepositoryImplTest{

    lateinit var dvtOpenWeatherDatabase: DvtOpenWeatherDatabase
    lateinit var forecastDao: ForecastDao
    lateinit var forecastsRepository: ForecastsRepository

    @Before
    fun initDB(){

        dvtOpenWeatherDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DvtOpenWeatherDatabase::class.java)
            .build()

        forecastDao = dvtOpenWeatherDatabase.forecastDao()
        forecastsRepository = ForecastsRepositoryImpl(forecastDao)

    }

    @After
    fun closeDb() {
        dvtOpenWeatherDatabase.close()
    }

    @Test
    fun forecast_repository_impl_returns_only_six_records_when_called() = runTest {

        // create dummy forecast Entities & add to DB
        forecastsRepository.insertForecasts(getEightForecastEntitiesInList())

        var flowResultsSize = forecastsRepository.getForecasts().first().toList().size

        assertEquals(6, flowResultsSize)

    }

    @Test
    fun forecast_repository_impl_returns_desc_order_list() = runTest {

        // initial favorite value is set to false
        forecastsRepository.insertForecasts(getEightForecastEntitiesInList())

        var flowResultList = forecastsRepository.getForecasts().first().toList()

        assertTrue(flowResultList[0].id > flowResultList[flowResultList.size - 1].id)

    }

    private fun getEightForecastEntitiesInList(): List<ForecastEntity>{

        var forecastEntity = ForecastEntity(
            id = 0,
            forecastDate = "",
            weather = "",
            weatherForecastTime = "",
            minimumTemperature = 0.0,
            currentTemperature = 0.0,
            maximumTemperature = 0.0,
            humidity = 0.0,
            city = "",
            country = "",
            latitude = 0.0,
            longitude = 0.0,
            isFavourite = false
        )

        return listOf(
            forecastEntity,
            forecastEntity,
            forecastEntity,
            forecastEntity,
            forecastEntity,
            forecastEntity,
            forecastEntity,
            forecastEntity,
        )

    }

}