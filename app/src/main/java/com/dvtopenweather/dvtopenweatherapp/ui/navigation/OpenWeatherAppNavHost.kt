
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OpenWeatherAppNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        homeScreen()
        favouritesScreen()
        mapsScreen()
        aboutScreen()
    }
}
