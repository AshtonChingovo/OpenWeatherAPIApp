package com.dvtopenweather.dvtopenweatherapp.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dvtopenweather.dvtopenweatherapp.R
import com.dvtopenweather.dvtopenweatherapp.data.repository.model.ForecastEntity
import com.dvtopenweather.dvtopenweatherapp.ui.theme.QuickSand
import com.dvtopenweather.dvtopenweatherapp.util.STANDARD_DATE_FORMAT
import com.dvtopenweather.dvtopenweatherapp.util.STANDARD_DATE_FORMAT_SECONDS
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeekForecastDetailsDialog(
    forecastEntitiesList: List<ForecastEntity>,
    showDialogState: () -> Unit
) {
    AlertDialog(
        modifier = Modifier.padding(16.dp),
        onDismissRequest = { showDialogState() },
        title = {
            Text(
                stringResource(id = R.string.week_forecast_label),
                textAlign = TextAlign.Center,
                fontFamily = QuickSand,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                forecastEntitiesList.forEach{
                    // format to get day
                    val day = LocalDateTime.parse(it.forecastDate, DateTimeFormatter.ofPattern(STANDARD_DATE_FORMAT_SECONDS))
                        .format(DateTimeFormatter.ofPattern("EEEE"))

                    Text(
                        text = day,
                        fontFamily = QuickSand,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    WeekWeatherDetailsSection(forecast = it)
                }
            }
        },
        confirmButton = {}
    )
}

@Composable
fun WeekWeatherDetailsSection(forecast: ForecastEntity) {
    Row(modifier = Modifier.padding(16.dp)) {
        WeekWeatherDetailsSectionItems(
            forecast.minimumTemperature,
            stringResource(id = R.string.minimum_temperature),
            Alignment.Start,
            Modifier.weight(1f)
        )
        WeekWeatherDetailsSectionItems(
            forecast.currentTemperature,
            stringResource(id = R.string.current_temperature),
            Alignment.CenterHorizontally,
            Modifier.weight(1f)
        )
        WeekWeatherDetailsSectionItems(
            forecast.maximumTemperature,
            stringResource(id = R.string.maximum_temperature),
            Alignment.End,
            Modifier.weight(1f)
        )
    }
}

@Composable
fun WeekWeatherDetailsSectionItems(
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
        Text("$temperature\u00B0", textAlign = TextAlign.Center)
        Text(temperatureLabel, textAlign = TextAlign.Center)
    }
}