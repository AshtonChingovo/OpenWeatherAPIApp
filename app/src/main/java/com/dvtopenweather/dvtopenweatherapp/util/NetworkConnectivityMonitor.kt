package com.dvtopenweather.dvtopenweatherapp.util

import kotlinx.coroutines.flow.Flow

interface NetworkConnectivityMonitor {
    val isConnected: Flow<Boolean>
}