package com.dvtopenweather.dvtopenweatherapp.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dvtopenweather.dvtopenweatherapp.R
import com.dvtopenweather.dvtopenweatherapp.data.repository.model.ForecastEntity
import com.dvtopenweather.dvtopenweatherapp.ui.LocationSettingsUIState
import com.dvtopenweather.dvtopenweatherapp.ui.LocationSettingsViewModel
import com.dvtopenweather.dvtopenweatherapp.ui.screens.util.MessageUI
import com.dvtopenweather.dvtopenweatherapp.ui.theme.*
import com.dvtopenweather.dvtopenweatherapp.util.CLOUDS
import com.dvtopenweather.dvtopenweatherapp.util.RAIN
import com.dvtopenweather.dvtopenweatherapp.util.STANDARD_DATE_FORMAT
import com.dvtopenweather.dvtopenweatherapp.util.STANDARD_DATE_FORMAT_SECONDS
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeRoute(
    locationSettingsViewModel: LocationSettingsViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val homeUIState = homeViewModel.homeUIState.collectAsStateWithLifecycle()

    // check location settings
    locationSettingsViewModel.checkLocationSettings()

    var locationSettingsState = locationSettingsViewModel.locationSettingsUIState.collectAsStateWithLifecycle()

    if (locationSettingsState.value == LocationSettingsUIState.LocationSettingsON) {
        when(homeUIState.value){
            HomeUIState.Loading -> MessageUI(
                imageResourceId = R.drawable.no_connection,
                heading = stringResource(id = R.string.fetching_forecast),
                message = stringResource(id = R.string.fetching_forecast_message),
                showLoadingAnimation = true,
                showRetryButton = false,
                onClick = {}
            )
            HomeUIState.Failed -> MessageUI(
                imageResourceId = R.drawable.caution,
                heading = stringResource(id = R.string.forecasts_fetch_failed),
                message = stringResource(id = R.string.forecasts_fetch_failed_message),
                showLoadingAnimation = false,
                showRetryButton = true,
                onClick = {}
            )
            HomeUIState.NoSavedData -> MessageUI(
                imageResourceId = R.drawable.saved_forecasts,
                heading = stringResource(id = R.string.saved_forecasts),
                message = stringResource(id = R.string.saved_forecasts_message),
                showLoadingAnimation = false,
                showRetryButton = false,
                onClick = {}
            )
            HomeUIState.NoConnection -> MessageUI(
                imageResourceId = R.drawable.no_connection,
                heading = stringResource(id = R.string.no_connection),
                message = stringResource(id = R.string.no_connection_message),
                showLoadingAnimation = false,
                showRetryButton = true,
                onClick = {}
            )
            is HomeUIState.Success -> {
                val sortedForecast = (homeUIState.value as HomeUIState.Success).forecasts.sortedBy { it.id } as MutableList<ForecastEntity>
                Home(sortedForecast, homeViewModel)
            }
        }
    } else {
        // LocationSettingsOff(onClick = locationSettingsViewModel::checkLocationSettings)
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(
    forecasts: MutableList<ForecastEntity>,
    homeViewModel: HomeViewModel
){

    val showWeekForecastDetailsDialog = remember { mutableStateOf(false) }
    val showForecastTimeExplanationDialog = remember { mutableStateOf(false) }

    if (showWeekForecastDetailsDialog.value) {
        WeekForecastDetailsDialog(forecasts) { showWeekForecastDetailsDialog.value = false }
    }

    if (showForecastTimeExplanationDialog.value) {
        ForeCastTimeExplanationDialog{ showForecastTimeExplanationDialog.value = false }
    }

    var forecastToday = forecasts[0]
    var forecastTime = LocalDateTime.parse(
            forecastToday.forecastDate,
            DateTimeFormatter.ofPattern(STANDARD_DATE_FORMAT_SECONDS))
            .format(DateTimeFormatter.ofPattern("HH:mm"))
            .toString()

    var forecastLastUpdated = LocalDateTime.parse(
        forecastToday.forecastDate,
        DateTimeFormatter.ofPattern(STANDARD_DATE_FORMAT_SECONDS))
        .format(DateTimeFormatter.ofPattern("dd, MMM"))
        .toString()

    val brush = gradientBrush(forecastToday.weather)

    Column(
        modifier = Modifier
            .background(brush = brush)
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        HeaderSection(forecastLastUpdated, forecastToday) { homeViewModel.updateForecastFavouriteStatus(forecastToday) }
        WeatherDetailsSection(forecastToday)
        Divider(color = OffWhite, modifier = Modifier.alpha(0.3f))
        WeatherForecastTimeMoreDetailsSection(
            forecastTime,
            forecastTimeOnClick = { showForecastTimeExplanationDialog.value = true },
            moreOnClick = { showWeekForecastDetailsDialog.value = true }
        )
        Divider(color = OffWhite, modifier = Modifier.alpha(0.3f))
        WeatherForecastSection(forecastToday.weather, forecasts)
    }
}

@Composable
fun HeaderSection(
    forecastLastUpdated: String,
    forecast: ForecastEntity,
    showDialogState: () -> Unit
) {
    Box {
        Image(
            painter = painterResource(id = gradientBackgroundImageResourceId(forecast.weather)),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )
        Column(
            modifier = Modifier.matchParentSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
                    .padding(16.dp)
            ) {
                Text(forecast.city, color = OffWhite)
                IconButton(onClick = showDialogState) {
                    Icon(
                        painter = painterResource(id =
                            if(forecast.isFavourite)
                                R.drawable.filled_favorite_white
                            else
                                R.drawable.outline_favorite_white),
                        contentDescription = "",
                        tint = OffWhite
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            ) {
                Text(
                    "${forecast.currentTemperature}\u00B0",
                    color = OffWhite,
                    fontFamily = QuickSand,
                    fontSize = 42.sp
                )
                Text(
                    forecast.weather,
                    color = OffWhite,
                    fontFamily = QuickSand,
                    fontSize = 42.sp
                )
                Text(forecastLastUpdated, color = OffWhite)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherForecastTimeMoreDetailsSection(
    time: String,
    forecastTimeOnClick: () -> Unit,
    moreOnClick: () -> Unit
) {
    Row(modifier = Modifier
        .padding(horizontal = 16.dp, vertical = 12.dp)
        .fillMaxWidth()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(id = R.string.forecast_time_label),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = OffWhite
            )
            Text(
                time,
                textAlign = TextAlign.Center,
                color = OffWhite
            )
        }
        IconButton(onClick = forecastTimeOnClick) {
            Icon(
                Icons.Default.Info,
                contentDescription = "",
                tint = OffWhite
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        OutlinedButton(
            border = BorderStroke(0.5.dp, OffWhite),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = OffWhite
            ),
            onClick = moreOnClick,
            modifier = Modifier.align(alignment = Alignment.Bottom)
        ) {
            Text(
                stringResource(id = R.string.more_label),
                fontSize = 12.sp,
                color = OffWhite,
                fontWeight = FontWeight.Light,
                fontFamily = QuickSand
            )
        }
    }
}

@Composable
fun WeatherDetailsSection(forecast: ForecastEntity) {
    Row(modifier = Modifier.padding(16.dp)) {
        WeatherDetailsSectionItems(
            forecast.minimumTemperature,
            stringResource(id = R.string.minimum_temperature),
            Alignment.Start,
            Modifier.weight(1f)
        )
        WeatherDetailsSectionItems(
            forecast.currentTemperature,
            stringResource(id = R.string.current_temperature),
            Alignment.CenterHorizontally,
            Modifier.weight(1f)
        )
        WeatherDetailsSectionItems(
            forecast.maximumTemperature,
            stringResource(id = R.string.maximum_temperature),
            Alignment.End,
            Modifier.weight(1f)
        )
    }
}

@Composable
fun WeatherDetailsSectionItems(
    temperature: Double,
    temperatureLabel: String,
    alignment: Alignment.Horizontal,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = alignment
    ) {
        Text("$temperature\u00B0", textAlign = TextAlign.Center, color = OffWhite)
        Text(temperatureLabel, textAlign = TextAlign.Center, color = OffWhite)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherForecastSection(weather: String, forecast: MutableList<ForecastEntity>) {
    // remove the first forecast i.e today's forecast
    val weeklyForecast = forecast.toMutableList()
    weeklyForecast.removeFirst()

    Box() {
        Column(modifier = Modifier.verticalScroll(state = rememberScrollState())) {
            weeklyForecast.forEach { dayForecast ->
                WeatherForecastSectionItems(dayForecast)
            }
        }
        Image(
            painter = painterResource(id = surfaceBackgroundImageResourceId(weather)),
            contentDescription = "",
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .alpha(0.2f)
                .align(alignment = Alignment.BottomEnd)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherForecastSectionItems(forecast: ForecastEntity) {

    var day = LocalDateTime.parse(forecast.forecastDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        .format(DateTimeFormatter.ofPattern("EEEE"))

    Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Text(
            text = day,
            color = OffWhite,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f)
        )
        Image(
            painter = painterResource(id = getIconResourceId(forecast.weather)),
            contentDescription = null,
            modifier = Modifier.size(28.dp),
        )
        Text(
            text = "${forecast.currentTemperature}°",
            color = OffWhite,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

fun gradientBrush(weather: String): Brush {

    return when(weather){
        "Rain" -> Brush.verticalGradient(listOf(Rainy, Rainy, RainyDark))
        "Clouds" -> Brush.verticalGradient(listOf(Cloudy, Cloudy, CloudyDark))
        else -> Brush.verticalGradient(listOf(Sunny, Sunny, SunnyDark))
    }

}

fun gradientBackgroundImageResourceId(weather: String): Int {

    return when(weather){
        RAIN -> R.drawable.forest_rainy
        CLOUDS -> R.drawable.forest_cloudy
        else -> R.drawable.forest_sunny
    }

}

fun surfaceBackgroundImageResourceId(weather: String): Int {

    return when(weather){
        RAIN -> R.drawable.rainy_background
        CLOUDS -> R.drawable.cloudy_background
        else -> R.drawable.sun
    }

}

fun getIconResourceId(weather: String): Int{

    return when(weather){
        RAIN -> R.drawable.rain_39
        CLOUDS -> R.drawable.partlysunny_39
        else -> R.drawable.clear_39
    }

}