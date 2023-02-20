package com.dvtopenweather.dvtopenweatherapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.dvtopenweather.dvtopenweatherapp.ui.DvtOpenWeatherApp
import com.dvtopenweather.dvtopenweatherapp.ui.theme.DVTOpenWeatherTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val systemUiController = rememberSystemUiController()

            systemUiController.setSystemBarsColor(color = Color.Transparent)
            systemUiController.systemBarsDarkContentEnabled = true

            WindowCompat.setDecorFitsSystemWindows(window, false)

            DVTOpenWeatherTheme {
                DvtOpenWeatherApp()
            }
        }
    }
}