package com.dvtopenweather.dvtopenweatherapp.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dvtopenweather.dvtopenweatherapp.R
import com.dvtopenweather.dvtopenweatherapp.ui.theme.QuickSand

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForeCastTimeExplanationDialog(showDialogState: () -> Unit) {
    AlertDialog(
        modifier = Modifier.padding(16.dp),
        onDismissRequest = { showDialogState() },
        title = {
            Text(
                stringResource(id = R.string.forecast_time_dialog_label),
                textAlign = TextAlign.Center,
                fontFamily = QuickSand,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                stringResource(id = R.string.forecast_time_dialog_description),
                textAlign = TextAlign.Center,
                fontFamily = QuickSand
            )

        },
        confirmButton = {}
    )
}