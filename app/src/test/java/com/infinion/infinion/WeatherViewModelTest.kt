package com.infinion.infinion

import com.infinion.infinion.data.model.Main
import com.infinion.infinion.data.model.Sys
import com.infinion.infinion.data.model.WeatherItem
import com.infinion.infinion.data.model.WeatherResponse
import com.infinion.infinion.db.entity.CityEntity
import com.infinion.infinion.utils.LocalHelper.getCountryName
import junit.framework.TestCase.assertEquals
import org.junit.Test

class WeatherViewModelTest {


    @Test
    fun `create CityEntity from API response`() {
        val weatherResponse = WeatherResponse(
            name = "London",
            sys = Sys(country = "GB"),
            main = Main(temp = 20.5f, feels_like = 18.0f),
            weather = listOf(WeatherItem(description = "Clear sky", icon = "01d"))
        )

        val city = weatherResponse.weather?.get(0)?.let {
            CityEntity(
                name = weatherResponse.name.toString(),
                country = getCountryName(weatherResponse.sys?.country.toString()),
                description = it.description.toString(),
                temperature = weatherResponse.main?.temp.toString().toFloat(),
                feelsLike = weatherResponse.main?.feels_like.toString().toFloat(),
                icon = it.icon.toString()
            )
        }

        assertEquals("London", city?.name)
        assertEquals("United Kingdom", city?.country)
        assertEquals("Clear sky", city?.description)
        assertEquals(20.5f, city?.temperature)
        assertEquals(18.0f, city?.feelsLike)
        assertEquals("01d", city?.icon)
    }

}
