package com.dvtopenweather.dvtopenweatherapp.ui
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dvtopenweather.dvtopenweatherapp.util.FLOW_WHILE_SUBSCRIBED_TIMEOUT
import com.dvtopenweather.dvtopenweatherapp.util.NetworkConnectivityMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NetworkMonitorViewModel @Inject constructor(
    networkConnectivityMonitor: NetworkConnectivityMonitor,
) : ViewModel() {

    // listen to changes in network connectivity
    val internetConnectivity: Flow<Boolean> = networkConnectivityMonitor.isConnected
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(FLOW_WHILE_SUBSCRIBED_TIMEOUT),
            initialValue = false
        )

    init {
        // start listening to flow
        // networkConnectivityMonitor.isConnected
    }

}
