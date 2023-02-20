package com.dvtopenweather.dvtopenweatherapp.ui.screens.favourites.locationDialog

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dvtopenweather.dvtopenweatherapp.data.remote.GooglePlacesLocationData
import com.dvtopenweather.dvtopenweatherapp.data.remote.model.googlePlaces.GooglePlacesResource
import com.dvtopenweather.dvtopenweatherapp.di.APIKeysModule
import com.dvtopenweather.dvtopenweatherapp.di.NetworkModule
import com.dvtopenweather.dvtopenweatherapp.util.NetworkConnectivityMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class LocationDetailsViewModel @Inject constructor(
    var googlePlacesLocationData: GooglePlacesLocationData,
    var connectivityManager: NetworkConnectivityMonitor,
    var ioDispatcher: CoroutineDispatcher,
    @APIKeysModule.GooglePlacesKEY var googleApiKey: String
) : ViewModel() {

    var locationDetailsUIState: MutableStateFlow<LocationDetailsUIState> = MutableStateFlow(LocationDetailsUIState.Loading)

    fun getGooglePlacesData(location: String){

        viewModelScope.launch(ioDispatcher) {

            connectivityManager.isConnected.collect{

                if(it){

                    val googlePlacesData = googlePlacesLocationData.getLocationDetails(location, googleApiKey)

                    if(googlePlacesData!!.googlePlacesDetailsResource.isEmpty())
                        locationDetailsUIState.emit(LocationDetailsUIState.Failed)
                    else
                        locationDetailsUIState.emit(LocationDetailsUIState.Success(googlePlacesData))
                }
                else
                    locationDetailsUIState.emit(LocationDetailsUIState.NoConnection)

            }
        }
    }


}

sealed interface LocationDetailsUIState{
    object Loading: LocationDetailsUIState
    object Failed: LocationDetailsUIState
    object NoConnection: LocationDetailsUIState
    data class Success(val googlePlacesResource: GooglePlacesResource): LocationDetailsUIState
}


