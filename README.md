# OpenWeather App

OpenWeather app is an android weather application that displays a five day weather forecast starting on the current day of request. It uses the user's current location to show the most relevant forecast for their current location.

# Features
Features in the app include allowing the user to save locations that can be viewed offline, a map with a pin of the user's current precise location with the added option of viewing additional weather information for where the user precisely is. Google Places API integration to get details of saved locations. 

The 5 day forecast features the weather details that include the min, current and max expected temperatures of that day

# Screenshots 

![weather_app](https://user-images.githubusercontent.com/33720666/226706552-4429f8b1-424a-499e-808b-716a31636b2d.jpg)


# Tools 

Main tools used in building this app include

* Kotlin
* Jetpack Compose
* Hilt
* Room 
* Retrofit
* Koltin Flows
* Junit 

# Architecture 

OpenWeather is built using the MVVM architecture 

# Configuration

Please add your own Google Places & OpenWeather API Keys to the local.properties file with the following respective variable names

GOOGLE_MAPS_API_KEY

OPEN_WEATHER_API_KEY
