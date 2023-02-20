package com.dvtopenweather.dvtopenweatherapp.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dvtopenweather.dvtopenweatherapp.data.remote.OpenWeatherForecastData
import com.dvtopenweather.dvtopenweatherapp.data.remote.model.openWeather.OpenWeatherDailyForecastResource
import com.dvtopenweather.dvtopenweatherapp.data.remote.model.openWeather.OpenWeatherFiveDayForecastResource
import com.dvtopenweather.dvtopenweatherapp.data.repository.ForecastsRepository
import com.dvtopenweather.dvtopenweatherapp.data.repository.LocationsRepository
import com.dvtopenweather.dvtopenweatherapp.data.repository.model.ForecastEntity
import com.dvtopenweather.dvtopenweatherapp.data.repository.model.LocationEntity
import com.dvtopenweather.dvtopenweatherapp.di.APIKeysModule
import com.dvtopenweather.dvtopenweatherapp.domain.InsertForecastsUseCase
import com.dvtopenweather.dvtopenweatherapp.util.*
import com.dvtopenweather.dvtopenweatherapp.util.NetworkConnectivityMonitor
import com.sample.android_sample_preference_datastore.UserDataAndLatestUpdateData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeViewModel @Inject constructor(
    var forecastsRepository: ForecastsRepository,
    var locationsRepository: LocationsRepository,
    var openWeatherForecastData: OpenWeatherForecastData,
    var ioDispatcher: CoroutineDispatcher,
    var protoDataStore: DataStore<UserDataAndLatestUpdateData>,
    var insertForecastsUseCase: InsertForecastsUseCase,
    networkConnectivityMonitor: NetworkConnectivityMonitor,
    @APIKeysModule.OpenWeatherAPIKEY openWeatherAPIKEY: String
) : ViewModel() {

    var homeUIState: StateFlow<HomeUIState> = forecastsRepository.getForecasts().map {

        if(it.isNotEmpty())
            HomeUIState.Success(it)
        else if(it.isEmpty())
            HomeUIState.NoSavedData
        else
            HomeUIState.Failed

    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(FLOW_WHILE_SUBSCRIBED_TIMEOUT),
            HomeUIState.Loading
        )

    init {

        viewModelScope.launch(ioDispatcher) {

            // get latest forecast saved offline
            forecastsRepository.getForecasts()

            // listen for network connectivity changes
            networkConnectivityMonitor.isConnected.collect{ isConnected ->

                // the app will only attempt to fetch weather data after every 10 min if online i.e to handle instances when the user navigates back & forth from the homeScreen
                // fetch latest success data updated time from protoDataStore
                if(isConnected){
                    protoDataStore.data.catch { exception ->
                        if (exception is IOException) {
                            emit(UserDataAndLatestUpdateData.getDefaultInstance())
                            TODO("update UI with error state")
                        } else {
                            throw exception
                        }
                    }.collect{

                        if(
                            isForecastUpdateNeeded(it.latestSuccessfulUpdate) &&
                            it.latitude != 0.0 &&
                            it.longitude != 0.0
                        ){

                            val forecastsList = openWeatherForecastData.fiveDayWeatherForecast(
                                it.latitude.toString(),
                                it.longitude.toString(),
                                openWeatherAPIKEY
                            )

                            // parse the list to local ForecastEntities & add to repository
                            insertForecastsUseCase(getFilteredForecastsLists(forecastsList))

                            // update the protoDataStore to the current date for last successful update
                            val date = LocalDateTime.now().format(DateTimeFormatter.ofPattern(STANDARD_DATE_FORMAT))
                            protoDataStore.updateData {store ->
                                store.toBuilder()
                                    .setLatestSuccessfulUpdate(date)
                                    .build()
                            }
                        }
                    }
                }

            }

        }
    }

    fun updateForecastFavouriteStatus(forecast: ForecastEntity){
        viewModelScope.launch(ioDispatcher) {

            val isFavouriteUpdate = !forecast.isFavourite
            forecastsRepository.updateForecastIsFavourite(isFavourite = isFavouriteUpdate, forecast.id)

            // add new location to list
            if(isFavouriteUpdate){
                locationsRepository.insertLocation(
                   LocationEntity(
                       id = 0,
                       forecastEntityId = forecast.id,
                       dateUpdated = LocalDateTime.now().format(DateTimeFormatter.ofPattern(STANDARD_DATE_FORMAT)),
                       city = forecast.city,
                       country = forecast.country,
                       longitude = forecast.longitude,
                       latitude = forecast.latitude,
                       isFavourite = true
                   )
                )
            }
            // remove from list
            else{
                locationsRepository.deleteLocation(forecast.id)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isForecastUpdateNeeded(lastUpdateTime: String): Boolean {

        // fetch data from UI
        if(lastUpdateTime.isEmpty())
            return true

        return getTimeDifference(lastUpdateTime)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTimeDifference(lastUpdatedDate: String): Boolean{

        val standardDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(STANDARD_DATE_FORMAT)
        val dayDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd")
        val hourTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH")
        val minuteDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("mm")

        val formattedLastUpdatedDate = LocalDateTime.parse(lastUpdatedDate, standardDateTimeFormatter)
        val currentDate: LocalDateTime = LocalDateTime.now()

        // check a difference in date
        if(currentDate.format(dayDateTimeFormatter) != formattedLastUpdatedDate.format(dayDateTimeFormatter))
            return true
        // check difference in hours
        else if(currentDate.format(hourTimeFormatter) != formattedLastUpdatedDate.format(hourTimeFormatter))
            return true
        // check for more than 10 minute difference in time
        else{

            val lastUpdatedDateMinutes = formattedLastUpdatedDate.format(minuteDateTimeFormatter).toInt()

            val currentMinutes = currentDate.format(minuteDateTimeFormatter).toInt()

            return if(currentMinutes >= lastUpdatedDateMinutes){
                (currentMinutes - lastUpdatedDateMinutes) > MINUTES_10
            } else{
                ((MINUTES_60-lastUpdatedDateMinutes) + currentMinutes) > MINUTES_10
            }

        }

    }

    // objects filtered by hour closest to current hour
    private fun getFilteredForecastsLists(fiveDayForecast: OpenWeatherFiveDayForecastResource): OpenWeatherFiveDayForecastResource {

        val hourTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH")

        val currentHour = LocalDateTime.now().format(hourTimeFormatter).toInt()
        val forecastList = mutableListOf<OpenWeatherDailyForecastResource>()

        // get list of all distinct dates from json response i.e the six dates for the forecast
        val distinctDates = fiveDayForecast.forecast.map {
            it.date.substring(0, it.date.indexOf(" "))
        }.distinctBy { it }.toList()

        // get weather forecast object closest less than or equal to the current if available
        // else get the closest hour behind current hour
        distinctDates.forEach {

            val forecastsList: List<OpenWeatherDailyForecastResource> = fiveDayForecast.forecast

            // double filtering to make code more readable
            // filter list to only weather forecasts objects with the current distinct date e.g only 19/02/2023
            // filter list to objects 3 hours or less than the current hour
            // sort by descending order of time (using unixTimeStamp) to get the one's closest & select that one
            // e.g if current hour is 17:00 find objects 3 hours or less & pick the one
            val filteredForecast: OpenWeatherDailyForecastResource = forecastsList
                .filter { obj -> obj.date.startsWith(it) }
                .filter { forecastObject -> (forecastObject.date.substring(forecastObject.date.indexOf(" ") + 1, forecastObject.date.indexOf(":")).toInt() - currentHour) <= HOURS_3 }
                .sortedByDescending { curr -> curr.unixTimeStamp }[0]

            forecastList.add(filteredForecast)

        }

        fiveDayForecast.forecast = forecastList

        return fiveDayForecast

    }

}

sealed interface HomeUIState{
    object Loading: HomeUIState
    object Failed: HomeUIState
    object NoSavedData: HomeUIState
    object NoConnection: HomeUIState
    data class Success(val forecasts: List<ForecastEntity>): HomeUIState
}

