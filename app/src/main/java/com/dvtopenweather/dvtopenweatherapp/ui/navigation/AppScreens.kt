import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dvtopenweather.dvtopenweatherapp.ui.screens.favourites.FavouritesRoute
import com.dvtopenweather.dvtopenweatherapp.ui.screens.home.HomeRoute

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.homeScreen(){
    composable(route = "home"){ HomeRoute() }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.favouritesScreen(){
    composable(route = "favourites"){ FavouritesRoute() }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.mapsScreen(){
    composable(route = "map"){ MapRoute() }
}

fun NavGraphBuilder.aboutScreen() {
    composable(route = "about") { AboutRoute() }
}