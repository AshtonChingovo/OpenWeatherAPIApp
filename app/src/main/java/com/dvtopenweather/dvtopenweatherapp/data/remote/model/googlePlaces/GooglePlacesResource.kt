package com.dvtopenweather.dvtopenweatherapp.data.remote.model.googlePlaces

import com.google.gson.annotations.SerializedName

data class GooglePlacesResource(
    @SerializedName("results")
    var googlePlacesDetailsResource: List<GooglePlacesDetailsResource>,
)

data class GooglePlacesDetailsResource(
    @SerializedName("formatted_address")
    var address: String,
    @SerializedName("geometry")
    var geometry: GooglePlacesCoordinates,
    @SerializedName("name")
    var placeName: String,
    @SerializedName("place_id")
    var placeId: String
)

data class GooglePlacesCoordinates(
    @SerializedName("location")
    var geometry: GooglePlaceGeoCoordinates
)

data class GooglePlaceGeoCoordinates(
    @SerializedName("lat")
    var latitude: Double,
    @SerializedName("lng")
    var longitude: Double,
)
