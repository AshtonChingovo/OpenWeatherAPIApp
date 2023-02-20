package com.dvtopenweather.dvtopenweatherapp.data.repository.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val forecastEntityId: Int,
    val dateUpdated: String,
    val city: String,
    val country: String,
    val longitude: Double,
    val latitude: Double,
    val isFavourite: Boolean
)
