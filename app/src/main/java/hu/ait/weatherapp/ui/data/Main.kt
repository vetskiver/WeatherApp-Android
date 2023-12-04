package hu.ait.weatherapp.ui.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Main(
    @SerialName("feels_like")
    var feelsLike: Double? = null,
    @SerialName("humidity")
    var humidity: Int? = null,
    @SerialName("pressure")
    var pressure: Int? = null,
    @SerialName("temp")
    var temp: Double? = null,
    @SerialName("temp_max")
    var tempMax: Double? = null,
    @SerialName("temp_min")
    var tempMin: Double? = null
)