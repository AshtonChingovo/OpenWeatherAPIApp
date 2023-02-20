package com.dvtopenweather.dvtopenweatherapp.di

import android.content.Context
import com.dvtopenweather.dvtopenweatherapp.util.NetworkConnectivityManager
import com.dvtopenweather.dvtopenweatherapp.util.NetworkConnectivityMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkConnectivityMonitorModule {

    @Provides
    @Singleton
    fun providesNetworkConnectivityMonitor(@ApplicationContext applicationContext: Context):
            NetworkConnectivityMonitor = NetworkConnectivityManager(applicationContext)

}