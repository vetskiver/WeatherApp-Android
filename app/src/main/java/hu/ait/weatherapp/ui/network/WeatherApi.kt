package hu.ait.weatherapp.ui.network

import hu.ait.weatherapp.ui.data.Main
import hu.ait.weatherapp.ui.data.WeatherDetails
import retrofit2.http.GET
import retrofit2.http.Query

// https://openweathermap.org/api/latest?access_key=c15e633ddf60f9e3b82c86ae689d1fe4
// HOST: https://openweathermap.org/api
// PATH: data/2.5/weather
// QUERY paras: ?access_key=c15e633ddf60f9e3b82c86ae689d1fe4

interface WeatherApi {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("q") location: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): WeatherDetails
}