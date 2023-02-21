package com.dvtopenweather.dvtopenweatherapp.data.repository

import com.dvtopenweather.dvtopenweatherapp.data.repository.model.LocationEntity
import kotlinx.coroutines.flow.Flow

interface LocationsRepository {

    fun insertLocation(location: LocationEntity)

    fun deleteLocationById(id: Int)

    fun deleteLocationById(location: LocationEntity)

    fun getLocations(): Flow<List<LocationEntity>>

}