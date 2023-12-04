package hu.ait.weatherapp.ui.screen

import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import hu.ait.weatherapp.ui.data.Main
import hu.ait.weatherapp.ui.data.WeatherDetails
import hu.ait.weatherapp.ui.theme.WeatherAppTheme

@Composable
fun WeatherDetailsScreen(cityName: String, viewModel: WeatherDetailsViewModel = hiltViewModel()) {

    // Assuming the API call should be triggered when the screen is created
    LaunchedEffect(Unit) {
        viewModel.getWeather(cityName)
    }

    Column {
        // Display other weather details based on the API response
        when (viewModel.weatherUIState) {
            is WeatherUiState.Init -> Text(text = "Press the button...")
            is WeatherUiState.Loading -> CircularProgressIndicator()
            is WeatherUiState.Success -> ResultView(
                weatherApp = (viewModel.weatherUIState as WeatherUiState.Success).weatherApp)
            is WeatherUiState.Error ->
                Text(text = "Error:" +
                        " ${(viewModel.weatherUIState as WeatherUiState.Error).errorMsg}")
        }
    }
}

@Composable
fun MapView(weatherApp: WeatherDetails) {
    // Create a LatLng object using the city's coordinates
    val cityLatLng = LatLng(weatherApp.coord?.lat ?: 0.0, weatherApp.coord?.lon ?: 0.0)

    // Set up the GoogleMap composable with a marker for the city
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(cityLatLng, 10f)
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxSize()
            .height(200.dp), // Adjust the height as needed
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = cityLatLng),
            title = weatherApp.name ?: "Unknown City",
            snippet = "Weather in ${weatherApp.name}"
        )
    }
}

@Composable
fun ResultView(weatherApp: WeatherDetails) {
    Column {
        Text(text = "City: ${weatherApp.name}")

        Text(text = "Temp: ${weatherApp.main?.temp}")
        Text(text = "Description: ${weatherApp.weather!!.get(0)!!.description}")

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://openweathermap.org/img/w/${
                    weatherApp.weather?.get(0)?.icon
                }.png")
                .crossfade(true)
                .build(),
            contentDescription = "Image",
            modifier = Modifier
                .size(100.dp)
        )

    }
}