package com.dvtopenweather.dvtopenweatherapp.data.repository.dao

import androidx.room.*
import com.dvtopenweather.dvtopenweatherapp.data.repository.model.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Insert
    fun insertLocation(location: LocationEntity)

    @Query("SELECT * FROM locations ORDER BY id DESC")
    fun getLocations(): Flow<List<LocationEntity>>

    @Query("DELETE FROM locations WHERE forecastEntityId = :id")
    fun deleteLocation(id: Int)

    @Delete
    fun deleteLocation(location: LocationEntity)
}