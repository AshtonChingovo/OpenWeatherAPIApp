package com.dvtopenweather.dvtopenweatherapp.data.remote.model.openWeather

import com.google.gson.annotations.SerializedName

data class OpenWeatherFiveDayForecastResource(

    @SerializedName("list")
    var forecast: List<OpenWeatherDailyForecastResource>,
    @SerializedName("city")
    var city: OpenWeatherCityResource

)

data class OpenWeatherDailyForecastResource(

    @SerializedName("dt")
    var unixTimeStamp: Long,
    @SerializedName("main")
    var temperatureDetailsResource: OpenWeatherTemperatureWeatherDetailsResource,
    @SerializedName("weather")
    var weatherDetailsResource: List<OpenWeatherWeatherResource>,
    @SerializedName("dt_txt")
    var date: String,

    )

data class OpenWeatherTemperatureWeatherDetailsResource(

    @SerializedName("temp")
    var currentTemperature: Double,
    @SerializedName("temp_min")
    var maxTemperature: Double,
    @SerializedName("temp_max")
    var minTemperature: Double,

)

data class OpenWeatherWeatherResource(
    @SerializedName("main")
    var weather: String,
)

data class OpenWeatherCityResource(
    @SerializedName("name")
    var city: String,
    @SerializedName("country")
    var country: String,
    @SerializedName("coord")
    var coordinates: UserCoordinates,
)

data class  UserCoordinates(
    @SerializedName("lat")
    var latitude: Double,
    @SerializedName("lon")
    var longitude: Double,
)

