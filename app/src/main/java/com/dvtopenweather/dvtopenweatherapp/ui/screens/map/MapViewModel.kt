package com.dvtopenweather.dvtopenweatherapp.ui.screens.map

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dvtopenweather.dvtopenweatherapp.data.repository.LocationsRepository
import com.dvtopenweather.dvtopenweatherapp.data.repository.model.ForecastEntity
import com.dvtopenweather.dvtopenweatherapp.data.repository.model.LocationEntity
import com.dvtopenweather.dvtopenweatherapp.domain.GetCurrentWeatherForecastUseCase
import com.dvtopenweather.dvtopenweatherapp.util.*
import com.dvtopenweather.dvtopenweatherapp.util.NetworkConnectivityMonitor
import com.google.android.gms.location.*
import com.sample.android_sample_preference_datastore.UserDataAndLatestUpdateData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// permissions are asked for by DvtOpenWeatherApp
@SuppressLint("MissingPermission")
@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class MapViewModel@Inject constructor(
    @ApplicationContext context: Context,
    var locationsRepository: LocationsRepository,
    var getCurrentWeatherForecastUseCase: GetCurrentWeatherForecastUseCase,
    var ioDispatcher: CoroutineDispatcher,
    var protoDataStore: DataStore<UserDataAndLatestUpdateData>,
    private var networkConnectivityMonitor: NetworkConnectivityMonitor,
): ViewModel() {

    private lateinit var locationCallback: LocationCallback
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    // request location update after every 5 mins
    var locationRequest: LocationRequest = LocationRequest.Builder(
        LOCATION_REQUEST_INTERVAL
    ).build()

    // user current location
    var dialogUIState: MutableStateFlow<DialogUIState> = MutableStateFlow(DialogUIState.Loading)

    // get saved locations
    var mapUIState: Flow<MapUIState> = locationsRepository.getLocations()
        .map { locations ->

            // delay a little to allow the GoogleMap to load up
            delay(MAP_LOADING_DELAY_INTERVAL)

            if(locations.isEmpty())
                MapUIState.Failed
            else{
                MapUIState.Success(locations)
            }

        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(FLOW_WHILE_SUBSCRIBED_TIMEOUT),
            initialValue = MapUIState.Loading
        )

    // user current location
    var userLocationState: MutableStateFlow<UserLocationState> = MutableStateFlow(UserLocationState.Waiting)

    init {
        // fetch saved locations
        viewModelScope.launch(ioDispatcher){

            // get user current location
            // get the user location & update protoDataStore
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location = locationResult.locations[0]

                    updateUserCurrentLocationDetails(location.latitude, location.longitude)

                }
            }

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

        }
    }

    fun updateUserCurrentLocationDetails(
        latitude: Double,
        longitude: Double
    ){

        viewModelScope.launch {

            // update protoDataStore with latest user location
            protoDataStore.updateData {store ->
                store.toBuilder()
                    .setLatitude(latitude)
                    .setLongitude(longitude)
                    .build()
            }

            // emit UserLocationState
            userLocationState.emit(UserLocationState.UserCurrentLocation(latitude, longitude))

        }

    }

    fun fetchCurrentLocationWeatherForecast(
        latitude: Double,
        longitude: Double
    ){

        viewModelScope.launch(ioDispatcher) {

            // show loading UI
            dialogUIState.emit(DialogUIState.Loading)

            // fetch data only if user is connected to an active network
            // listen for network connectivity changes
            networkConnectivityMonitor.isConnected.collect{ isConnected ->

                // the app will only attempt to fetch weather data after every 10 mins if online i.e to handle instances when the user
                // navigates back & forth from the homeScreen fetch latest success data updated time from protoDataStore
                if(isConnected){

                    var currentForecastEntity = getCurrentWeatherForecastUseCase(latitude = latitude, longitude = longitude)

                    dialogUIState.emit(DialogUIState.Success(currentWeatherForecast = currentForecastEntity))

                }
                else
                    dialogUIState.emit(DialogUIState.NoConnection)

            }

        }

    }


}

sealed interface MapUIState{
    object Loading: MapUIState
    object Failed: MapUIState
    data class Success(val savedLocations: List<LocationEntity>): MapUIState
}

sealed interface UserLocationState{
    object Waiting: UserLocationState
    data class UserCurrentLocation(val latitude: Double, var longitude: Double): UserLocationState
}

sealed interface DialogUIState{
    object Loading: DialogUIState
    object Failed: DialogUIState
    object NoConnection: DialogUIState
    data class Success(val currentWeatherForecast: ForecastEntity): DialogUIState
}