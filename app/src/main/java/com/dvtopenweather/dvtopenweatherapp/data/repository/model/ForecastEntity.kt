package com.dvtopenweather.dvtopenweatherapp.data.repository.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecasts")
data class ForecastEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val forecastDate: String,
    val weather: String,
    var weatherForecastTime: String,
    val minimumTemperature: Double,
    val currentTemperature: Double,
    val maximumTemperature: Double,
    val humidity: Double = 0.0,
    val city: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val isFavourite: Boolean
)
