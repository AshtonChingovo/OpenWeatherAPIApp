package com.dvtopenweather.dvtopenweatherapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.dvtopenweather.dvtopenweatherapp.data.datastore.UserLocationSerializer
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
class DataStoreModule {

    @Provides
    @Singleton
    fun providesDataStore(@ApplicationContext context: Context): DataStore<UserDataAndLatestUpdateData> = DataStoreFactory.create(
        serializer = UserLocationSerializer(),
        produceFile = { context.dataStoreFile("") },
        corruptionHandler = null
    )

}