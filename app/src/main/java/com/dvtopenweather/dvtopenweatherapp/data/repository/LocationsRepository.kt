package com.dvtopenweather.dvtopenweatherapp.data.repository

import com.dvtopenweather.dvtopenweatherapp.data.repository.model.LocationEntity
import kotlinx.coroutines.flow.Flow

interface LocationsRepository {

    fun insertLocation(location: LocationEntity)

    fun deleteLocation(id: Int)

    fun deleteLocation(location: LocationEntity)

    fun getLocations(): Flow<List<LocationEntity>>

}