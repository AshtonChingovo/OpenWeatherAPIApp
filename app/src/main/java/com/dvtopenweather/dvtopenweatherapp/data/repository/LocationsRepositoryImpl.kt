package com.dvtopenweather.dvtopenweatherapp.data.repository

import com.dvtopenweather.dvtopenweatherapp.data.repository.dao.LocationDao
import com.dvtopenweather.dvtopenweatherapp.data.repository.model.LocationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationsRepositoryImpl @Inject constructor(
    var locationsDao: LocationDao
): LocationsRepository {

    override fun insertLocation(location: LocationEntity) {
        locationsDao.insertLocation(location)
    }

    override fun deleteLocationById(id: Int) {
        locationsDao.deleteLocation(id)
    }

    override fun deleteLocationById(location: LocationEntity) {
        locationsDao.deleteLocation(location)
    }

    override fun getLocations(): Flow<List<LocationEntity>> = locationsDao.getLocations()

}