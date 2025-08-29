package com.infinion.infinion.data.repository

import com.infinion.infinion.data.model.WeatherResponse
import retrofit2.Response

interface IWeatherRepository {
    suspend fun getWeather(city: String, apiKey: String): Response<WeatherResponse>
}
