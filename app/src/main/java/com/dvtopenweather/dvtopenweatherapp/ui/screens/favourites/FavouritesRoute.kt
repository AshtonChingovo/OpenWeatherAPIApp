package com.dvtopenweather.dvtopenweatherapp.ui.screens.favourites

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dvtopenweather.dvtopenweatherapp.R
import com.dvtopenweather.dvtopenweatherapp.data.repository.model.LocationEntity
import com.dvtopenweather.dvtopenweatherapp.ui.screens.favourites.locationDialog.LocationDetailsDialog
import com.dvtopenweather.dvtopenweatherapp.ui.screens.util.MessageUI
import com.dvtopenweather.dvtopenweatherapp.ui.theme.*
import com.dvtopenweather.dvtopenweatherapp.util.STANDARD_DATE_FORMAT
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FavouritesRoute(
    favouritesViewModel: FavouritesViewModel = hiltViewModel()
) {

    val favouritesUIState = favouritesViewModel.favouritesUIState.collectAsStateWithLifecycle()

    Surface(
        color = OffWhite,
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
            .fillMaxHeight()
    ) {
        when(favouritesUIState.value){
            FavouritesUIState.Loading -> MessageUI(
                imageResourceId = R.drawable.no_connection,
                heading = stringResource(id = R.string.fetching_forecast),
                message = stringResource(id = R.string.fetching_forecast_message),
                showLoadingAnimation = true,
                showRetryButton = false,
                onClick = {}
            )
            FavouritesUIState.Failed -> MessageUI(
                imageResourceId = R.drawable.caution,
                heading = stringResource(id = R.string.forecasts_fetch_failed),
                message = stringResource(id = R.string.forecasts_fetch_failed_message),
                showLoadingAnimation = false,
                showRetryButton = true,
                onClick = {}
            )
            FavouritesUIState.NoSavedData -> MessageUI(
                imageResourceId = R.drawable.saved_forecasts,
                heading = stringResource(id = R.string.saved_locations),
                message = stringResource(id = R.string.saved_locations_message),
                showLoadingAnimation = false,
                showRetryButton = false,
                onClick = {}
            )
            FavouritesUIState.NoConnection -> MessageUI(
                imageResourceId = R.drawable.no_connection,
                heading = stringResource(id = R.string.no_connection),
                message = stringResource(id = R.string.no_connection_message),
                showLoadingAnimation = false,
                showRetryButton = true,
                onClick = {}
            )
            is FavouritesUIState.Success -> {
                val sortedForecast = (favouritesUIState.value as FavouritesUIState.Success)
                    .favourites
                    .sortedBy{ it.id } as MutableList<LocationEntity>
                Favourites(sortedForecast, favouritesViewModel)
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Favourites(
    locations: List<LocationEntity>,
    favouritesViewModel: FavouritesViewModel
){

    var showDialog = remember { mutableStateOf(false) }
    var locationEntityIndex = remember { mutableStateOf(0) }

    if(showDialog.value){
        LocationDetailsDialog(locations[locationEntityIndex.value].city) {
            showDialog.value = false
        }
    }

    Box(){
        Column {
            HeaderSection(locations.size)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(items = locations) {index, location ->
                    FavouritesListItems(
                        index, location,
                        onDialogShow = {
                            favouritesViewModel.clickedIndex(index)
                            showDialog.value = true
                        },
                        onDeleteFavourites = {favouritesViewModel.deleteLocation(location)})
                }
            }
        }
        Image(
            painter = painterResource(id = R.drawable.favourites_background),
            contentDescription = "",
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .alpha(0.3f)
                .size(512.dp)
                .align(alignment = Alignment.BottomEnd)
        )
    }

}

@Composable
fun HeaderSection(count: Int) {

    var label = if(count == 1) stringResource(id = R.string.location_label) else stringResource(id = R.string.locations_label)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ){
        Text(
            count.toString(),
            fontSize = 32.sp
        )
        Text(" $label")
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FavouritesListItems(
    index: Int,
    locationEntity: LocationEntity,
    onDialogShow: () -> Unit,
    onDeleteFavourites: () -> Unit
){

    var brush = getItemGradient(index)

    Card(shape = RoundedCornerShape(20.dp)){
        Column(modifier = Modifier.background(brush = brush)) {
            TopDetailsSection(locationEntity, onDialogShow)
            Spacer(Modifier)
            BottomDetailsSection(locationEntity, onDeleteFavourites)
        }
    }

}

@Composable
fun TopDetailsSection(
    locationEntity: LocationEntity,
    onClick: () -> Unit
){

    Row(Modifier.padding(16.dp)){
        Column(
            Modifier
                .weight(2f)
                .padding(end = 12.dp)) {
            Text(
                "${locationEntity.city} ",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                locationEntity.country,
                fontSize = 14.sp,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(
            onClick = onClick,
            modifier = Modifier.then(Modifier.size(24.dp)),
            content = {
                Icon(
                    Icons.Outlined.Info,
                    contentDescription = "",
                    tint = Color.White,
                )
            }
        )
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomDetailsSection(
    locationEntity: LocationEntity,
    onDeleteFavourites: () -> Unit
){

    val standardDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(STANDARD_DATE_FORMAT)
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd")
    val dayDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE")
    val monthDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM")

    // parse the day
    val date = LocalDateTime.parse(locationEntity.dateUpdated, standardDateTimeFormatter)

    val day = date.format(dateTimeFormatter)
    val dayOfTheWeek = date.format(dayDateTimeFormatter)
    val month = date.format(monthDateTimeFormatter)

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp )) {
        Text(
            textAlign = TextAlign.Center,
            text = day.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            color = Color.White
        )
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(2f)) {
            Text(
                dayOfTheWeek,
                fontSize = 14.sp,
                color = Color.White
            )
            Text(
                month.toString(),
                fontSize = 14.sp,
                color = Color.White
            )
        }
        IconButton(
            onClick = onDeleteFavourites,
            modifier = Modifier.then(Modifier.size(24.dp)),
            content = {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = "",
                    tint = OffWhite
                )
            }
        )
    }

}

fun getItemGradient(index: Int): Brush{

    return when(index % 4){
        0 -> Brush.sweepGradient(listOf(DVTBlueLight, DVTBlue))
        1 -> Brush.sweepGradient(listOf(OrangeLight, OrangeDark))
        2 -> Brush.sweepGradient(listOf(YellowLight, YellowDark))
        3 -> Brush.sweepGradient(listOf(TealLight, TeaDark))
        else -> Brush.sweepGradient(listOf(TealLight, TeaDark))
    }

}
