package hu.ait.weatherapp.ui.screen

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.weatherapp.ui.data.Main
import hu.ait.weatherapp.ui.data.WeatherDetails
import hu.ait.weatherapp.ui.network.WeatherApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherDetailsViewModel @Inject constructor(
    private val weatherAPI: WeatherApi
) : ViewModel() {
    var weatherUIState: WeatherUiState by mutableStateOf(WeatherUiState.Init)


    fun getWeather(query: String){
        weatherUIState = WeatherUiState.Loading

        viewModelScope.launch {
            try {
                val weatherApp = weatherAPI.getWeather(query,
                    "metric",
                    "31f2889bbc248fdd15004ad5ef5f9879")
                weatherUIState = WeatherUiState.Success(weatherApp)
            } catch (e: Exception) {
                weatherUIState = WeatherUiState.Error(e.message!!)
            }

        }
    }
}

sealed interface WeatherUiState {
    object Init : WeatherUiState
    object Loading : WeatherUiState
    data class Success(val weatherApp: WeatherDetails) : WeatherUiState
    data class Error(val errorMsg: String) : WeatherUiState
}