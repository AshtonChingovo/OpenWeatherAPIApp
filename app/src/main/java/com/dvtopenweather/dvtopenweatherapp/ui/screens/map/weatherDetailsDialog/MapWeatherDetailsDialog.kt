package com.dvtopenweather.dvtopenweatherapp.ui.screens.map.weatherDetailsDialog

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dvtopenweather.dvtopenweatherapp.ui.theme.*
import com.dvtopenweather.dvtopenweatherapp.R
import com.dvtopenweather.dvtopenweatherapp.data.repository.model.ForecastEntity
import com.dvtopenweather.dvtopenweatherapp.ui.screens.map.DialogUIState
import com.dvtopenweather.dvtopenweatherapp.ui.screens.map.MapViewModel
import com.dvtopenweather.dvtopenweatherapp.ui.screens.util.MessageUI
import com.dvtopenweather.dvtopenweatherapp.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MapWeatherDetailsDialog(
    latitude: Double,
    longitude: Double,
    onClick: () -> Unit,
    weatherDetailsDialogViewModel: MapViewModel,
) {

    val currentWeatherForecastState = weatherDetailsDialogViewModel.dialogUIState.collectAsStateWithLifecycle()
    weatherDetailsDialogViewModel.fetchCurrentLocationWeatherForecast(latitude, longitude)

    val color = getWeatherDialogColor(currentWeatherForecastState.value)

    AlertDialog(
        modifier = Modifier.fillMaxHeight(),
        backgroundColor = color,
        onDismissRequest = { onClick() },
        title = {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.current_forecast_label),
                    fontFamily = QuickSand,
                    fontSize = 16.sp,
                    color = OffWhite
                )
            }
        },
        text = {
               Column(
                   verticalArrangement = Arrangement.Center,
                   modifier = Modifier.wrapContentHeight()
               ) {
                   when(currentWeatherForecastState.value){
                       DialogUIState.Loading -> MessageUI(
                           imageResourceId = R.drawable.forest_rainy,
                           heading = stringResource(id = R.string.fetching_forecast),
                           message = stringResource(id = R.string.fetching_forecast_message),
                           showLoadingAnimation = true,
                           showRetryButton = false,
                           onClick = {}
                       )
                       DialogUIState.NoConnection -> MessageUI(
                           imageResourceId = R.drawable.no_connection,
                           heading = stringResource(id = R.string.no_connection),
                           message = stringResource(id = R.string.no_connection_message),
                           showLoadingAnimation = false,
                           showRetryButton = true,
                           // retry fetch operation
                           onClick = { weatherDetailsDialogViewModel.fetchCurrentLocationWeatherForecast(latitude, longitude) }
                       )
                       DialogUIState.Failed -> MessageUI(
                           imageResourceId = R.drawable.caution,
                           heading = stringResource(id = R.string.maps_dialog_data_fetch_failed),
                           message = stringResource(id = R.string.maps_dialog_data_fetch_failed_message),
                           showLoadingAnimation = false,
                           showRetryButton = true,
                           // retry fetch operation
                           onClick = {weatherDetailsDialogViewModel.fetchCurrentLocationWeatherForecast(latitude, longitude)}
                       )
                       is DialogUIState.Success -> CurrentForecast(
                           forecastEntity = (currentWeatherForecastState.value as DialogUIState.Success).currentWeatherForecast
                       )
                   }
               }
        },
        confirmButton = {},
    )
}

@Composable
fun CurrentForecast(forecastEntity: ForecastEntity){

    Box(Modifier.fillMaxHeight()){
        Column {
            WeatherDetailsMainSection(forecastEntity)
            Divider(
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.4f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            WeatherDetailsHumidityPressureSection(forecastEntity)
            WeatherDetail(
                stringResource(id = R.string.maps_dialog_country),
                forecastEntity.country,
                Modifier.fillMaxWidth()
            )
        }
        Image(
            painter = painterResource(id = surfaceBackgroundImageResourceId(forecastEntity.weather)),
            contentDescription = "",
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .alpha(0.3f)
                .align(alignment = Alignment.TopEnd)
        )
        Image(
            painter = painterResource(id = surfaceBackgroundImageResourceId(forecastEntity.weather)),
            contentDescription = "",
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .alpha(0.7f)
                .align(alignment = Alignment.BottomCenter)
        )
        Spacer(modifier = Modifier.height(32.dp))
    }

}

@Composable
fun WeatherDetailsMainSection(forecastEntity: ForecastEntity) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = forecastEntity.weather,
            fontFamily = QuickSand,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = OffWhite
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "${forecastEntity.currentTemperature}°",
            fontFamily = QuickSand,
            fontSize = 62.sp,
            fontWeight = FontWeight.Bold,
            color = OffWhite
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun WeatherDetailsHumidityPressureSection(forecastEntity: ForecastEntity) {
    Column {
        Text(stringResource(
            id = R.string.current_weather_label),
            fontFamily = QuickSand,
            fontWeight = FontWeight.Bold,
            color = OffWhite
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            WeatherDetail(
                stringResource(id = R.string.maps_dialog_current_temperature),
                forecastEntity.currentTemperature.toString(),
                Modifier.weight(1f)
            )
            Divider(
                Modifier
                    .fillMaxHeight()
                    .padding(vertical = 12.dp)
                    .alpha(0.4f)
                    .width(1.dp),
                color = Color.White)
            WeatherDetail(
                stringResource(id = R.string.maps_dialog_max_temperature),
                forecastEntity.maximumTemperature.toString(),
                Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            )
        }
        Divider(color = Color.White, modifier = Modifier.alpha(0.4f))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            WeatherDetail(
                stringResource(id = R.string.maps_dialog_min_temperature),
                forecastEntity.minimumTemperature.toString(),
                Modifier.weight(1f)
            )
            Divider(
                Modifier
                    .fillMaxHeight()
                    .padding(vertical = 12.dp)
                    .alpha(0.4f)
                    .width(1.dp),
                color = Color.White
            )
            WeatherDetail(
                stringResource(id = R.string.maps_dialog_humidity),
                forecastEntity.humidity.toString(),
                Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            )
        }
        Divider(
            color = Color.White,
            modifier = Modifier.alpha(0.4f)
        )
    }
}

@Composable
fun WeatherDetail(parameter: String, value: String, modifier: Modifier) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(vertical = 16.dp)
    ) {
        Column {
            Text(
                text = parameter,
                fontFamily = QuickSand,
                fontWeight = FontWeight.Medium,
                color = OffWhite
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontFamily = QuickSand,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = OffWhite
            )
        }
    }
}

fun getWeatherDialogColor(dialogUIState: DialogUIState): Color {

    return when(dialogUIState){
        DialogUIState.Loading -> Color.White
        DialogUIState.NoConnection -> Color.White
        DialogUIState.Failed -> Color.White
        is DialogUIState.Success -> {
            when(dialogUIState.currentWeatherForecast.weather){
                RAIN -> Rainy
                CLOUDS -> Cloudy
                else -> Sunny
            }
        }
    }

}

fun surfaceBackgroundImageResourceId(weather: String): Int {

    return when(weather){
        RAIN -> R.drawable.rainy_background
        CLOUDS -> R.drawable.cloudy_background
        else -> R.drawable.sun
    }

}
