package com.dvtopenweather.dvtopenweatherapp.ui.screens.favourites

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dvtopenweather.dvtopenweatherapp.data.repository.ForecastsRepository
import com.dvtopenweather.dvtopenweatherapp.data.repository.LocationsRepository
import com.dvtopenweather.dvtopenweatherapp.data.repository.model.LocationEntity
import com.dvtopenweather.dvtopenweatherapp.util.FLOW_WHILE_SUBSCRIBED_TIMEOUT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class FavouritesViewModel @Inject constructor(
    var locationsRepository: LocationsRepository,
    var forecastsRepository: ForecastsRepository,
    var ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    var clickedIndex: MutableStateFlow<Int> = MutableStateFlow<Int>(0)

    var favouritesUIState: StateFlow<FavouritesUIState> = locationsRepository.getLocations().map {

        if(it.isNotEmpty())
            FavouritesUIState.Success(it)
        else if(it.isEmpty())
            FavouritesUIState.NoSavedData
        else
            FavouritesUIState.Failed

    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(FLOW_WHILE_SUBSCRIBED_TIMEOUT),
            FavouritesUIState.Loading
        )

    init {

        viewModelScope.launch(ioDispatcher) {

            // get saved locations
            locationsRepository.getLocations()
        }
    }

    fun deleteLocation(location: LocationEntity){
        viewModelScope.launch(ioDispatcher) {
            locationsRepository.deleteLocation(location)
            // update value for forecast as well
            forecastsRepository.updateForecastIsFavourite(false, location.forecastEntityId)
        }
    }

    fun clickedIndex(index: Int){
        viewModelScope.launch(ioDispatcher) {
            clickedIndex.emit(index)
        }
    }

}

sealed interface FavouritesUIState{
    object Loading: FavouritesUIState
    object Failed: FavouritesUIState
    object NoSavedData: FavouritesUIState
    object NoConnection: FavouritesUIState
    data class Success(val favourites: List<LocationEntity>): FavouritesUIState
}


