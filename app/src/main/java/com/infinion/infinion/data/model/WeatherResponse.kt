package com.infinion.infinion.data.model

data class WeatherResponse(
    val name: String? = null,
    val main: Main? = null,
    val weather: List<WeatherItem>? = null,
    val sys: Sys? = null
)

data class Main(
    val temp: Float? = null,
    val feels_like: Float? = null,
    val temp_min: Float? = null,
    val temp_max: Float? = null,
    val humidity: Int? = null,
    val pressure: Int? = null
)

data class WeatherItem(
    val description: String? = null, val icon: String? = null
)

data class Sys(
    val country: String? = null,
    val id: Int? = null,
    val sunrise: Int? = null,
    val sunset: Int? = null,
    val type: Int? = null
)


