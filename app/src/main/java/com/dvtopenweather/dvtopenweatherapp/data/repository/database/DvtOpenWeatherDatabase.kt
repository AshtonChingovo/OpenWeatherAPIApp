package com.dvtopenweather.dvtopenweatherapp.data.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dvtopenweather.dvtopenweatherapp.data.repository.dao.ForecastDao
import com.dvtopenweather.dvtopenweatherapp.data.repository.dao.LocationDao
import com.dvtopenweather.dvtopenweatherapp.data.repository.model.ForecastEntity
import com.dvtopenweather.dvtopenweatherapp.data.repository.model.LocationEntity

@Database(
    entities = [
        ForecastEntity::class,
        LocationEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class DvtOpenWeatherDatabase : RoomDatabase() {

    abstract fun forecastDao(): ForecastDao

    abstract fun locationDao(): LocationDao

}