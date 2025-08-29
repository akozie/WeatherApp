package com.infinion.infinion.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.infinion.infinion.R
import com.infinion.infinion.data.model.WeatherResponse
import com.infinion.infinion.databinding.FragmentHomeBinding
import com.infinion.infinion.db.entity.CityEntity
import com.infinion.infinion.ui.detail.WeatherDetailActivity
import com.infinion.infinion.ui.home.HomeViewModel
import com.infinion.infinion.utils.Constants
import com.infinion.infinion.utils.LocalHelper.getCountryName
import com.infinion.infinion.utils.LocalHelper.getWeatherIcon
import com.infinion.infinion.utils.Resource
import com.infinion.infinion.utils.UtilityParam.API_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private var lastResult: WeatherResponse? = null
    private var searchJob: Job? = null
    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.emptyState.visibility = View.VISIBLE

        binding.fabSave.setOnClickListener {
            val city = binding.editCity.text.toString().trim()
            if (city.isNotBlank()) {
                val cityModel = lastResult?.weather?.get(0)?.let { it1 ->
                    CityEntity(
                        name = lastResult?.name.toString(),
                        country = getCountryName(lastResult?.sys?.country.toString()),
                        description = it1.description.toString(),
                        temperature = lastResult?.main?.temp.toString().toFloat(),
                        feelsLike = lastResult?.main?.feels_like.toString().toFloat(),
                        icon = it1.icon.toString(),
                    )
                }

                lastResult?.name?.let { it1 ->
                    if (cityModel != null) {
                        viewModel.toggleCitySaved(it1, cityModel)
                    }
                }

                // Toggle drawable depending on current state
                isFavorite = !isFavorite
                binding.fabSave.setImageResource(
                    if (isFavorite) R.drawable.ic_favorite_filled
                    else R.drawable.ic_favorite_border
                )

                Toast.makeText(
                    requireContext(),
                    if (isFavorite) "Saved $city to favorite" else "Removed $city from favorite",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        // Debounce input
        binding.editCity.addTextChangedListener { editable ->
            val city = editable.toString().trim()
            searchJob?.cancel()
            if (city.isNotBlank()) {
                searchJob = lifecycleScope.launch {
                    delay(800) // debounce delay (ms)
                    val apiKey = API_KEY
                    if (apiKey.isNotBlank()) {
                        viewModel.fetchWeather(city, apiKey)
                    }
                }
            } else {
                binding.resultCard.visibility = View.GONE
            }
        }

        // Card click → details screen
        binding.resultCard.setOnClickListener {
            lastResult?.let { res ->
                val desc = res.weather?.firstOrNull()?.description ?: ""
                val temp = res.main?.temp?.toString() ?: ""
                val i = Intent(requireContext(), WeatherDetailActivity::class.java).apply {
                    putExtra(Constants.EXTRA_CITY, res.name ?: binding.editCity.text.toString())
                    putExtra(Constants.EXTRA_COUNTRY,
                        res.sys?.country?.let { it1 -> getCountryName(it1) })
                    putExtra(Constants.EXTRA_TEMP, temp)
                    putExtra(Constants.EXTRA_FEEL_LIKE, res.main?.feels_like)
                    putExtra(Constants.EXTRA_LIKED, isFavorite)
                    putExtra(Constants.EXTRA_DESC, desc)
                    putExtra(Constants.EXTRA_ICON, res.weather?.get(0)?.icon)
                }
                startActivity(i)
            }
        }

        observeWeather()
    }

    private fun observeWeather() {
        viewModel.weather.observe(requireActivity()) { state ->
            when (state) {
                is Resource.Loading -> {
                    binding.progress.visibility = View.VISIBLE
                    binding.resultCard.visibility = View.GONE
                    binding.emptyState.visibility = View.GONE
                }

                is Resource.Success -> {
                    binding.progress.visibility = View.GONE
                    binding.fabSave.visibility = View.VISIBLE
                    binding.fabSave.setImageResource(R.drawable.ic_favorite_border)
                    isFavorite = false
                    val data = state.data
                    lastResult = data
                    val city = data.name ?: binding.editCity.text.toString()
                    val desc = data.weather?.firstOrNull()?.description ?: "N/A"
                    val temp = data.main?.temp?.toString() ?: "N/A"
                    val feelsLike = data.main?.feels_like?.toString() ?: "N/A"

                    binding.resultCard.visibility = View.VISIBLE
                    binding.emptyState.visibility = View.GONE
                    binding.tvCityName.text = city
                    binding.tvCountryName.text =
                        lastResult?.sys?.country?.let { getCountryName(it) }
                    binding.tvWeatherDescription.text = desc.replaceFirstChar { it.uppercase() }
                    binding.tvTemperature.text = "$temp °C"
                    binding.tvFeelsLike.text = "Feels like $feelsLike °C"
                    data.weather?.get(0).let {
                        it?.let { it.icon?.let { it1 -> getWeatherIcon(it1) } }
                            ?.let { it1 -> binding.ivWeatherIcon.setImageResource(it1) }
                    }
                }

                is Resource.Error -> {
                    binding.progress.visibility = View.GONE
                    binding.resultCard.visibility = View.GONE
                    binding.emptyState.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}