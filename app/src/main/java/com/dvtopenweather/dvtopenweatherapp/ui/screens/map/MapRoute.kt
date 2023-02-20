import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dvtopenweather.dvtopenweatherapp.R
import com.dvtopenweather.dvtopenweatherapp.data.repository.model.LocationEntity
import com.dvtopenweather.dvtopenweatherapp.ui.screens.map.MapUIState
import com.dvtopenweather.dvtopenweatherapp.ui.screens.map.MapViewModel
import com.dvtopenweather.dvtopenweatherapp.ui.screens.map.UserLocationState
import com.dvtopenweather.dvtopenweatherapp.ui.screens.map.weatherDetailsDialog.MapWeatherDetailsDialog
import com.dvtopenweather.dvtopenweatherapp.ui.screens.util.MessageUI
import com.dvtopenweather.dvtopenweatherapp.util.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MapRoute (
    mapViewModel: MapViewModel = hiltViewModel()
){

    val showDialog = remember { mutableStateOf(false) }
    val mapUIState = mapViewModel.mapUIState.collectAsStateWithLifecycle(MapUIState.Loading)
    val userLocationState = mapViewModel.userLocationState.collectAsStateWithLifecycle()

    // set initial camera position to southernAfrica & move camera once user current location is available
    val southernAfrica = LatLng(SOUTHERN_AFRICA_LATITUDE, SOUTHERN_AFRICA_LONGITUDE)

    val cameraPosition = when(userLocationState.value){
        UserLocationState.Waiting -> CameraPosition.fromLatLngZoom(southernAfrica, DEFAULT_MAP_ZOOM_LEVEL)
        is UserLocationState.UserCurrentLocation -> {
            val userCurrentLocation = (userLocationState.value as UserLocationState.UserCurrentLocation)

            CameraPosition.fromLatLngZoom(
                LatLng(userCurrentLocation.latitude, userCurrentLocation.longitude),
                MAP_ZOOM_LEVEL
            )
        }
    }

    if (showDialog.value) {

        if(userLocationState.value is UserLocationState.UserCurrentLocation){
            MapWeatherDetailsDialog(
                (userLocationState.value as UserLocationState.UserCurrentLocation).latitude,
                (userLocationState.value as UserLocationState.UserCurrentLocation).longitude,
                { showDialog.value = false },
                mapViewModel
            )
        }
    }

    Box {

        when(mapUIState.value){
            MapUIState.Loading -> MessageUI(
                imageResourceId = R.drawable.no_connection,
                heading = stringResource(id = R.string.setting_up_map),
                message = stringResource(id = R.string.setting_up_map_message),
                showLoadingAnimation = true,
                showRetryButton = false,
                onClick = {}
            )
            else -> Box{
                Map(cameraPosition, mapUIState.value) {
                    showDialog.value = true
                    true
                }
                TopMessageCard()
            }
        }
    }

}

@Composable
fun TopMessageCard(){
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(24.dp)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
    ) {
        Text(
            text = stringResource(id = R.string.map_top_message),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun Map(
    cameraPosition: CameraPosition,
    mapUIState: MapUIState,
    onClick: (Marker) -> Boolean
){

    val cameraPositionState = rememberCameraPositionState {
        position = cameraPosition
    }

    var savedLocations = mutableListOf<LocationEntity>()

    // get saved locations if UI state is Success i.e has a list of locations
    if(mapUIState is MapUIState.Success){
        savedLocations = mapUIState.savedLocations as MutableList<LocationEntity>
    }

    TopMessageCard()

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {

        // add user's current location to the list
        savedLocations.add(0, LocationEntity(
                id = 0,
                forecastEntityId = 0,
                dateUpdated = "",
                city = "",
                country = "",
                longitude = cameraPosition.target.longitude,
                latitude = cameraPosition.target.latitude,
                isFavourite = false
            )
        )

        savedLocations.forEach { location ->
            Marker(
                position = LatLng(location.latitude, location.longitude),
                title = location.city,
                snippet = "Saved ${location.city} location",
                onClick = onClick,
              )
        }

        LaunchedEffect(1) {
            if (cameraPositionState.isMoving) {
                // TODO: animate camera movement
                // move camera when there is an update to location
                cameraPositionState.move(CameraUpdateFactory.zoomIn())
            }
        }

    }

}