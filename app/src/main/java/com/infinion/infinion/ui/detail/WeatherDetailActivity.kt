package com.infinion.infinion.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.infinion.infinion.R
import com.infinion.infinion.databinding.ActivityDetailBinding
import com.infinion.infinion.utils.Constants
import com.infinion.infinion.utils.LocalHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val city = intent.getStringExtra(Constants.EXTRA_CITY) ?: ""
        val country = intent.getStringExtra(Constants.EXTRA_COUNTRY) ?: ""
        val desc = intent.getStringExtra(Constants.EXTRA_DESC) ?: ""
        val temp = intent.getStringExtra(Constants.EXTRA_TEMP) ?: ""
        val feelLike = intent.getStringExtra(Constants.EXTRA_FEEL_LIKE) ?: ""
        val isSaved = intent.getBooleanExtra(Constants.EXTRA_LIKED, false)
        val icon = intent.getStringExtra(Constants.EXTRA_ICON) ?: ""

        binding.cityName.text = city
        binding.countryName.text = country
        binding.favIcon.setImageResource(
            if (isSaved) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
        )

        binding.weatherDescription.text = desc.replaceFirstChar { it.uppercase() }
        binding.temperature.text = "$temp °C | feels like $feelLike °C"

        icon.let { LocalHelper.getWeatherIcon(it) }.let { it1 ->
            binding.weatherIcon.setImageResource(it1)
        }

    }
}
