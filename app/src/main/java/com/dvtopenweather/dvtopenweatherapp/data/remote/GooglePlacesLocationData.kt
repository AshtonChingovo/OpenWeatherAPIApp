package com.dvtopenweather.dvtopenweatherapp.data.remote

import com.dvtopenweather.dvtopenweatherapp.data.remote.model.googlePlaces.GooglePlacesResource

interface GooglePlacesLocationData {

    suspend fun getLocationDetails(location: String, apiKey: String): GooglePlacesResource?

}