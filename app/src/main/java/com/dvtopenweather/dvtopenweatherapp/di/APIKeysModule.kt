package com.dvtopenweather.dvtopenweatherapp.di

import com.dvtopenweather.dvtopenweatherapp.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class APIKeysModule {

    @OpenWeatherAPIKEY
    @Provides
    @Singleton
    fun providesOpenWeatherKey(): String = BuildConfig.OPEN_WEATHER_API_KEY

    @GooglePlacesKEY
    @Provides
    @Singleton
    fun providesGoogleMapsKey(): String = BuildConfig.GOOGLE_MAPS_API_KEY

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OpenWeatherAPIKEY

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class GooglePlacesKEY

}