package com.dvtopenweather.dvtopenweatherapp.data.remote

import com.dvtopenweather.dvtopenweatherapp.data.remote.api.GooglePlacesRetrofitAPI
import com.dvtopenweather.dvtopenweatherapp.data.remote.model.googlePlaces.GooglePlacesResource
import javax.inject.Inject

class GooglePlacesLocationDataImpl @Inject constructor(
    var googlePlacesAPI: GooglePlacesRetrofitAPI
): GooglePlacesLocationData {

    override suspend fun getLocationDetails(location: String, apiKey: String): GooglePlacesResource? {
        return try{
            googlePlacesAPI.getGooglePlaceLocation(location, apiKey)
        } catch (e: java.lang.Exception){
            null
        }
    }

}
