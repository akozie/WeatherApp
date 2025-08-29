package com.infinion.infinion.data.repository

import com.infinion.infinion.data.network.WeatherApi
import com.infinion.infinion.db.dao.CityDao
import com.infinion.infinion.db.entity.CityEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CityRepository @Inject constructor(
    private val cityDao: CityDao, private val weatherApi: WeatherApi
) {

    suspend fun saveCity(cityName: CityEntity) {
        cityDao.insertCity(
            CityEntity(
                name = cityName.name,
                country = cityName.country,
                description = cityName.description,
                temperature = cityName.temperature,
                feelsLike = cityName.feelsLike,
                icon = cityName.icon,
            )
        )
    }

    fun getAllCities(): Flow<List<CityEntity>> = cityDao.getAllCities()

    suspend fun getCityByName(name: String): CityEntity? = cityDao.getCityByName(name)

    suspend fun deleteCityByName(name: String) {
        val city = cityDao.getCityByName(name)
        if (city != null) {
            cityDao.deleteCity(city)
        }
    }

    suspend fun getWeather(city: String, apiKey: String) = weatherApi.getWeather(city, apiKey)
}
