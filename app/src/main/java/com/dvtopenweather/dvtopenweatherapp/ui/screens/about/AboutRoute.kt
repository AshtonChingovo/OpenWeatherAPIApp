
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.dvtopenweather.dvtopenweatherapp.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dvtopenweather.dvtopenweatherapp.ui.theme.OpenWeatherOrange
import com.dvtopenweather.dvtopenweatherapp.ui.theme.QuickSand

@Composable
fun AboutRoute() {

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(state = rememberScrollState())
            .padding(16.dp)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
            .background(Color.White)

    ) {
        SplashScreenHeaderSection()
        Image(
            painter = painterResource(id = R.drawable.dvt_logo),
            contentDescription = "",
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .size(200.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )
        SplashScreenFeaturesSections()
    }
}

@Composable
fun SplashScreenHeaderSection() {
    Column {
        Text(
            stringResource(id = R.string.open_weather_label),
            color = OpenWeatherOrange,
            fontSize = 28.sp,
            fontFamily = QuickSand,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))
        Text(
            stringResource(id = R.string.assignment_label),
            textAlign = TextAlign.Center,
            fontFamily = QuickSand
        )
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
fun SplashScreenFeaturesSections() {
    Column() {
        Spacer(Modifier.height(32.dp))
        Text(
            stringResource(id = R.string.features_label),
            fontFamily = QuickSand,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            FeatureItem(
                stringResource(id = R.string.jetpack_compose),
                R.drawable.jetpack_compose_logo,
                Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            Divider(
                Modifier
                    .fillMaxHeight()
                    .padding(vertical = 12.dp)
                    .width(1.dp),
                color = Color.LightGray
            )
            FeatureItem(
                stringResource(id = R.string.open_weather),
                R.drawable.openweather_logo,
                Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            )
        }
        Spacer(Modifier.height(16.dp))
        Divider(
            color = Color.LightGray,
            modifier = Modifier.alpha(0.4f)
        )
        Spacer(Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            FeatureItem(
                stringResource(id = R.string.kotlin),
                R.drawable.kotlin_logo,
                Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            Divider(
                Modifier
                    .fillMaxHeight()
                    .padding(vertical = 12.dp)
                    .width(1.dp),
                color = Color.LightGray
            )
            FeatureItem(
                stringResource(id = R.string.jetpack),
                R.drawable.jetpack_logo,
                Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            )
        }
    }
}

@Composable
fun FeatureItem(
    feature: String,
    imageResourceId: Int,
    modifier: Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(vertical = 16.dp)
    ) {
        Image(
            painter = painterResource(id = imageResourceId),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Spacer(
            Modifier
                .width(12.dp)
                .background(Color.LightGray)
        )
        Column {
            Text(
                text = feature,
                fontFamily = QuickSand
            )
        }
    }
}
