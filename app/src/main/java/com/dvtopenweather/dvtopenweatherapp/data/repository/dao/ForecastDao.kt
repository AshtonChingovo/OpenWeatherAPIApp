package com.dvtopenweather.dvtopenweatherapp.data.repository.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dvtopenweather.dvtopenweatherapp.data.repository.model.ForecastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDao {

    @Insert
    fun insertForecasts(forecasts: List<ForecastEntity>)

    @Query("SELECT * FROM forecasts ORDER BY id DESC LIMIT 6")
    fun getForecasts(): Flow<List<ForecastEntity>>

    @Query("UPDATE forecasts SET isFavourite = :isFavourite WHERE id = :id")
    fun updateFavouriteState(isFavourite: Boolean, id: Int)

}