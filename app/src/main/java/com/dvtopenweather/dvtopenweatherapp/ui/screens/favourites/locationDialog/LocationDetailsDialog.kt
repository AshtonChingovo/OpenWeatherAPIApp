package com.dvtopenweather.dvtopenweatherapp.ui.screens.favourites.locationDialog

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dvtopenweather.dvtopenweatherapp.R
import com.dvtopenweather.dvtopenweatherapp.data.remote.model.googlePlaces.GooglePlacesResource
import com.dvtopenweather.dvtopenweatherapp.ui.theme.QuickSand

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LocationDetailsDialog(
    city: String,
    locationDetailsViewModel: LocationDetailsViewModel = hiltViewModel(),
    showDialogState: () -> Unit
) {

    val locationDetailsUIState = locationDetailsViewModel.locationDetailsUIState.collectAsStateWithLifecycle()

    locationDetailsViewModel.getGooglePlacesData(city)

    AlertDialog(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        onDismissRequest = { showDialogState() },
        title = {
            Text(
                stringResource(id = R.string.google_places_label),
                textAlign = TextAlign.Center,
                fontFamily = QuickSand,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            when(locationDetailsUIState.value){
                LocationDetailsUIState.Loading -> Column(modifier = Modifier.height(200.dp)) {
                    LinearProgressIndicator()
                }
                LocationDetailsUIState.Failed -> Column(modifier = Modifier.height(200.dp)) {
                    Text(stringResource(id = R.string.data_fetch_failed))
                }
                LocationDetailsUIState.NoConnection -> Column(modifier = Modifier.height(200.dp)) {
                    Text(stringResource(id = R.string.no_connection))
                }
                is LocationDetailsUIState.Success -> {
                    val googlePlacesResource = (locationDetailsUIState.value as LocationDetailsUIState.Success).googlePlacesResource
                    LocationDialog(googlePlacesResource)
                }
            }
        },
        confirmButton = {}
    )
}

@Composable
fun LocationDialog(googlePlacesResource: GooglePlacesResource){
    Spacer(modifier = Modifier.height(16.dp))
    Column(Modifier.fillMaxHeight()
    ) {

        val coordinates = "${googlePlacesResource.googlePlacesDetailsResource[0].geometry.geometry.latitude}," +
                " ${googlePlacesResource.googlePlacesDetailsResource[0].geometry.geometry.longitude}"

        GooglePlaceDetailsItem(
            stringResource(id = R.string.google_places_address_label),
            googlePlacesResource.googlePlacesDetailsResource[0].address
        )
        GooglePlaceDetailsItem(
            stringResource(id = R.string.google_places_coordinates_label),
            coordinates
        )
        GooglePlaceDetailsItem(
            stringResource(id = R.string.google_places_place_name_label),
            googlePlacesResource.googlePlacesDetailsResource[0].placeName
        )
    }
}

@Composable
fun GooglePlaceDetailsItem(
    heading: String,
    body: String
) {
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
            .wrapContentSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Filled.LocationOn,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(heading)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(body)
        Spacer(modifier = Modifier.height(8.dp))

    }
}