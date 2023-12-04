package hu.ait.weatherapp.ui.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import hu.ait.weatherapp.ui.data.CityItem

import androidx.compose.runtime.mutableStateListOf
import hu.ait.weatherapp.ui.data.Main

class CityViewModel : ViewModel() {

    private var _cityList =
        mutableStateListOf<CityItem>()

    fun getAllCities(): List<CityItem> {
        return _cityList
    }

    fun getCityNum(): Int {
        return _cityList.size
    }

    fun addCity(cityItem: CityItem) {
        _cityList.add(cityItem)
    }

    fun removeCity(cityItem: CityItem) {
        _cityList.remove(cityItem)
    }

    fun removeAllCities() {
        _cityList.clear()
    }

}
