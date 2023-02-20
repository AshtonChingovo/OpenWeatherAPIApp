package com.dvtopenweather.dvtopenweatherapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.dvtopenweather.dvtopenweatherapp.data.datastore.UserLocationSerializer
import com.dvtopenweather.dvtopenweatherapp.data.remote.OpenWeatherForecastData
import com.dvtopenweather.dvtopenweatherapp.data.repository.ForecastsRepository
import com.dvtopenweather.dvtopenweatherapp.domain.GetCurrentWeatherForecastUseCase
import com.dvtopenweather.dvtopenweatherapp.domain.InsertForecastsUseCase
import com.sample.android_sample_preference_datastore.UserDataAndLatestUpdateData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCasesModule {

    @Provides
    @Singleton
    fun providesGetCurrentWeatherForecastUseCase(
        openWeatherForecastData: OpenWeatherForecastData,
        @APIKeysModule.OpenWeatherAPIKEY openWeatherAPIKEY: String
    ): GetCurrentWeatherForecastUseCase = GetCurrentWeatherForecastUseCase(openWeatherForecastData, openWeatherAPIKEY)


    @Provides
    @Singleton
    fun providesInsertForecastsUseCase(
        forecastsRepository: ForecastsRepository
    ): InsertForecastsUseCase = InsertForecastsUseCase(forecastsRepository)


}