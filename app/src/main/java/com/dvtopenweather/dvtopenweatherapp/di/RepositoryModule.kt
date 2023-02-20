package com.dvtopenweather.dvtopenweatherapp.di

import android.content.Context
import androidx.room.Room
import com.dvtopenweather.dvtopenweatherapp.data.repository.*
import com.dvtopenweather.dvtopenweatherapp.data.repository.dao.ForecastDao
import com.dvtopenweather.dvtopenweatherapp.data.repository.dao.LocationDao
import com.dvtopenweather.dvtopenweatherapp.data.repository.database.DvtOpenWeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun providesDvtOpenWeatherDatabase(@ApplicationContext context: Context): DvtOpenWeatherDatabase =  Room.databaseBuilder(
        context,
        DvtOpenWeatherDatabase::class.java,
        "dvt-open-weather-database"
    ).build()

    @Provides
    fun providesForecastDao(database: DvtOpenWeatherDatabase): ForecastDao = database.forecastDao()

    @Provides
    fun providesLocationsDao(database: DvtOpenWeatherDatabase): LocationDao = database.locationDao()

    @Singleton
    @Provides
    fun providesForecastsRepository(forecastDao: ForecastDao): ForecastsRepository = ForecastsRepositoryImpl(forecastDao)

    @Singleton
    @Provides
    fun providesLocationsRepository(locationDao: LocationDao): LocationsRepository = LocationsRepositoryImpl(locationDao)

}