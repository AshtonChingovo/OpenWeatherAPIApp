import com.dvtopenweather.dvtopenweatherapp.R

sealed class BottomNavigationMenuItem(
    val route: Int,
    val menuTitleResourceId: Int,
    val selectedIcon: Int,
    val unselectedIcon: Int
){
    object Home : BottomNavigationMenuItem(
        route = R.string.home_screen,
        menuTitleResourceId = R.string.home,
        selectedIcon = R.drawable.filled_home,
        unselectedIcon = R.drawable.outline_home
    )

    object Favourites : BottomNavigationMenuItem(
        route = R.string.favourites_screen,
        menuTitleResourceId = R.string.favourites,
        selectedIcon = R.drawable.filled_favorite,
        unselectedIcon = R.drawable.outline_favorite
    )

    object Map : BottomNavigationMenuItem(
        route = R.string.map_screen,
        menuTitleResourceId = R.string.map,
        selectedIcon = R.drawable.filled_map,
        unselectedIcon = R.drawable.outline_map
    )

    object About : BottomNavigationMenuItem(
        route = R.string.about_screen,
        menuTitleResourceId = R.string.about_screen,
        selectedIcon = R.drawable.filled_info,
        unselectedIcon = R.drawable.outline_info
    )
}