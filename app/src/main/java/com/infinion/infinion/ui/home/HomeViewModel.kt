package com.infinion.infinion.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.infinion.infinion.data.model.WeatherResponse
import com.infinion.infinion.data.repository.CityRepository
import com.infinion.infinion.data.repository.IWeatherRepository
import com.infinion.infinion.db.entity.CityEntity
import com.infinion.infinion.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: IWeatherRepository, private val repo: CityRepository
) : ViewModel() {

    private val _weather = MutableLiveData<Resource<WeatherResponse>>()
    val weather: LiveData<Resource<WeatherResponse>> = _weather

    fun fetchWeather(city: String, apiKey: String) {
        viewModelScope.launch {
            _weather.value = Resource.Loading
            try {
                val response = repository.getWeather(city, apiKey)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _weather.value = Resource.Success(body)
                    } else {
                        _weather.value = Resource.Error("Empty response")
                    }
                } else {
                    _weather.value = Resource.Error("HTTP ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                _weather.value = Resource.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }


    val savedCities: LiveData<List<CityEntity>> = repo.getAllCities().asLiveData()

    private fun saveCity(city: CityEntity) {
        viewModelScope.launch {
            repo.saveCity(city)
        }
    }


    // Toggle city saved/unsaved
    fun toggleCitySaved(cityName: String, city: CityEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val existing = cityName.let { repo.getCityByName(it) }
            if (existing != null) {
                repo.deleteCityByName(existing.name)
            } else {
                saveCity(city)
            }
        }
    }
}
