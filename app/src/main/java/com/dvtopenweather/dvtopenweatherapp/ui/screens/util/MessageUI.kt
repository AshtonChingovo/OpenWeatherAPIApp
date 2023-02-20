package com.dvtopenweather.dvtopenweatherapp.ui.screens.util

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dvtopenweather.dvtopenweatherapp.R
import com.dvtopenweather.dvtopenweatherapp.util.ROUNDED_CORNER_RADIUS_50

@Composable
fun MessageUI(
    imageResourceId: Int,
    heading: String,
    message: String,
    showLoadingAnimation: Boolean,
    showRetryButton: Boolean,
    onClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 16.dp)
    ) {

        if(showLoadingAnimation){
            LinearProgressIndicator()
        }
        else{
            Image(
                painter = painterResource(id = imageResourceId),
                contentDescription = null,
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(150.dp)

            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(heading, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            message,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        if(showRetryButton){
            OutlinedButton(
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(ROUNDED_CORNER_RADIUS_50),
                onClick = onClick
            ) {
                Text(stringResource(id = R.string.retry), color = Color.Black)
            }
        }
    }
}
