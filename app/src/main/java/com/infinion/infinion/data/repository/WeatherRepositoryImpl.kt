package com.infinion.infinion.data.repository

import com.infinion.infinion.data.model.WeatherResponse
import com.infinion.infinion.data.network.WeatherApi
import retrofit2.Response
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
) : IWeatherRepository {
    override suspend fun getWeather(city: String, apiKey: String): Response<WeatherResponse> {
        return api.getWeather(city, apiKey)
    }
}
