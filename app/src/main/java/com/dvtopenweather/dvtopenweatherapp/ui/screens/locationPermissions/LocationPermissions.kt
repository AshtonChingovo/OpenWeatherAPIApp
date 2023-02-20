package com.dvtopenweather.dvtopenweatherapp.ui.screens.locationPermissions

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dvtopenweather.dvtopenweatherapp.R
import com.dvtopenweather.dvtopenweatherapp.util.ROUNDED_CORNER_RADIUS_50
import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissions(locationPermissionsState: MultiplePermissionsState) {

    if (locationPermissionsState.allPermissionsGranted) {
        PermissionsGranted()
    } else {

        val allPermissionsRevoked = locationPermissionsState.permissions.size == locationPermissionsState.revokedPermissions.size

        if (!allPermissionsRevoked) {
            // only Course location permissions have been granted
            PermissionsRequest(
                stringResource(id = R.string.allow_precise_location_permission),
                onClick = { locationPermissionsState.launchMultiplePermissionRequest()}
            )
        } else if (locationPermissionsState.shouldShowRationale) {
            // Both location permissions have been denied
            PermissionsRequest(
                stringResource(id = R.string.allow_permissions),
                onClick = { locationPermissionsState.launchMultiplePermissionRequest()}
            )
        } else {

            // First time the user sees this feature or the user doesn't want to be asked again
            PermissionsRequest(
                stringResource(id = R.string.allow_permissions),
                onClick = { locationPermissionsState.launchMultiplePermissionRequest()}
            )
        }

    }
}

@Composable
fun PermissionsGranted() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            stringResource(id = R.string.ready_to_go),
            fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            stringResource(id = R.string.thank_you_for_granting_permissions),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun PermissionsRequest(
    buttonText: String,
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
        Text(stringResource(
            id = R.string.almost_ready_to_go),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            stringResource(id = R.string.permissions_rationale),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            border = BorderStroke(1.dp, Color.Black),
            shape = RoundedCornerShape(ROUNDED_CORNER_RADIUS_50),
            onClick = onClick
        ) {
            Text(buttonText, color = Color.Black)
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}
