package com.dvtopenweather.dvtopenweatherapp.data.remote.api

import com.dvtopenweather.dvtopenweatherapp.data.remote.model.googlePlaces.GooglePlacesResource
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesRetrofitAPI {

    @GET("json")
    suspend fun getGooglePlaceLocation(
        @Query("query") longitude: String,
        @Query("key") apiKey: String
    ): GooglePlacesResource

}