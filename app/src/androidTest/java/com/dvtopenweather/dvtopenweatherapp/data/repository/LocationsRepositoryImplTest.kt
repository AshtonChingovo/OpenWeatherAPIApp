package com.dvtopenweather.dvtopenweatherapp.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.dvtopenweather.dvtopenweatherapp.data.repository.dao.LocationDao
import com.dvtopenweather.dvtopenweatherapp.data.repository.database.DvtOpenWeatherDatabase
import com.dvtopenweather.dvtopenweatherapp.data.repository.model.LocationEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

internal class LocationsRepositoryImplTest {

    lateinit var dvtOpenWeatherDatabase: DvtOpenWeatherDatabase
    lateinit var locationDao: LocationDao
    lateinit var locationsRepository: LocationsRepository

    @Before
    fun initDB(){

        dvtOpenWeatherDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DvtOpenWeatherDatabase::class.java)
            .build()

        locationDao = dvtOpenWeatherDatabase.locationDao()
        locationsRepository = LocationsRepositoryImpl(locationDao)

    }

    @After
    fun closeDb() {
        dvtOpenWeatherDatabase.close()
    }

    @Test
    fun insertLocation() = runTest {

        locationsRepository.insertLocation(getLocationEntity())

        var locations = locationsRepository.getLocations().first()

        assertEquals(1, locations.size)
    }

    @Test
    fun deleteLocation() = runTest {

        locationsRepository.insertLocation(getLocationEntity())
        var forecastEntity = locationsRepository.getLocations().first()[0]
        locationsRepository.deleteLocationById(forecastEntity)

        var locationsList = locationsRepository.getLocations().first()

        assertTrue(locationsList.isEmpty())

    }

    @Test
    fun testDeleteLocationById() = runTest {

        locationsRepository.insertLocation(getLocationEntity())
        var forecastEntity = locationsRepository.getLocations().first()[0]
        locationsRepository.deleteLocationById(forecastEntity.forecastEntityId)

        var locationsList = locationsRepository.getLocations().first()

        assertTrue(locationsList.isEmpty())

    }

    fun getLocationEntity(): LocationEntity{
        return LocationEntity(
            id = 0,
            forecastEntityId = 0,
            dateUpdated = "",
            city = "",
            country = "",
            longitude = 0.0,
            latitude = 0.0,
            isFavourite = false
        )
    }

}