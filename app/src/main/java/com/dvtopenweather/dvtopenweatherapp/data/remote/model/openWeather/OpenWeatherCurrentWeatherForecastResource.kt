package com.dvtopenweather.dvtopenweatherapp.data.remote.model.openWeather

import com.google.gson.annotations.SerializedName

data class OpenWeatherCurrentWeatherForecastResource(

    @SerializedName("dt")
    var unixTimeStamp: Long,
    @SerializedName("weather")
    var weather: List<OpenWeatherCurrentDetailsResource>,
    @SerializedName("main")
    var temperature: OpenWeatherCurrentTemperatureDetailsResource,
    @SerializedName("sys")
    var country: OpenWeatherCountryResource,
    @SerializedName("name")
    var city: String

)

data class OpenWeatherCurrentTemperatureDetailsResource(

    @SerializedName("temp")
    var currentTemperature: Double,
    @SerializedName("temp_min")
    var maxTemperature: Double,
    @SerializedName("temp_max")
    var minTemperature: Double,
    @SerializedName("humidity")
    var humidity: Double,

)

data class OpenWeatherCountryResource(

    @SerializedName("country")
    var country: String

)

data class OpenWeatherCurrentDetailsResource(
    @SerializedName("main")
    var weather: String,
)

