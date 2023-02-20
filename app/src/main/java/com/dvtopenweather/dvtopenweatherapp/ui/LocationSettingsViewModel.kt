package com.dvtopenweather.dvtopenweatherapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dvtopenweather.dvtopenweatherapp.util.LOCATION_REQUEST_INTERVAL
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.sample.android_sample_preference_datastore.UserDataAndLatestUpdateData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationSettingsViewModel @Inject constructor(
    @ApplicationContext var context: Context,
    var protoDataStore: DataStore<UserDataAndLatestUpdateData>,
): ViewModel() {

    var locationSettingsUIState: MutableStateFlow<LocationSettingsUIState> = MutableStateFlow(
        LocationSettingsUIState.LocationSettingsON
    )

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private lateinit var locationCallback: LocationCallback

    // request location update after every 5 minutes
    var locationRequest: LocationRequest = LocationRequest.Builder(
        LOCATION_REQUEST_INTERVAL
    ).build()

    private val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    private val client: SettingsClient = LocationServices.getSettingsClient(context)
    var task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

    init {
        checkLocationSettings()
    }

    @SuppressLint("MissingPermission")
    fun checkLocationSettings() {

        task = client.checkLocationSettings(builder.build())

        // Check to see if user has their location settings set to on
        // If on update flow observers that the location updates are now being fetched
        task.addOnSuccessListener {
            updateLocationSettingsUIStateValue(true)

            // get the user location & update protoDataStore
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location = locationResult.locations[0]

                    // update protoDataStore with the latest user location
                    viewModelScope.launch {
                        protoDataStore.updateData {store ->
                            store.toBuilder()
                                .setLatitude(location.latitude)
                                .setLongitude(location.longitude)
                                .build()
                        }

                    }

                }
            }

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }

        // update observers that location settings are not set to on
        task.addOnFailureListener {
            updateLocationSettingsUIStateValue(false)
        }
    }

    private fun updateLocationSettingsUIStateValue(isLocationSettingsOn: Boolean) {
        viewModelScope.launch {
            locationSettingsUIState.emit(
                if (isLocationSettingsOn)
                    LocationSettingsUIState.LocationSettingsON
                else LocationSettingsUIState.LocationSettingsOFF
            )
        }
    }

}

sealed class LocationSettingsUIState {
    object LocationSettingsOFF : LocationSettingsUIState()
    object LocationSettingsON : LocationSettingsUIState()
}
