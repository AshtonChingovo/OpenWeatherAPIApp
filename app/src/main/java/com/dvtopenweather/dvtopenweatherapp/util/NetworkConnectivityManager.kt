package com.dvtopenweather.dvtopenweatherapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class NetworkConnectivityManager @Inject constructor(
    context: Context
): NetworkConnectivityMonitor {

    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // screens & viewModels us this flow to listen to network connectivity changes
    override val isConnected: Flow<Boolean> = callbackFlow {

        val callback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                channel.trySend(checkConnectivity(network))
            }

            override fun onLost(network: Network) {
                channel.trySend(checkConnectivity(network))
            }

            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                channel.trySend(checkConnectivity(network))
            }
        }

        connectivityManager.registerNetworkCallback(networkRequest, callback)

        channel.trySend(checkConnectivity(connectivityManager.activeNetwork))

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()

    fun checkConnectivity(network: Network?): Boolean = connectivityManager.getNetworkCapabilities(network)?.hasCapability(
        NetworkCapabilities.NET_CAPABILITY_INTERNET
    ) ?: false
}
