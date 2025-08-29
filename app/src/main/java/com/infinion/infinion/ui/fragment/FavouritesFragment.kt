package com.infinion.infinion.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinion.infinion.adapter.CitiesAdapter
import com.infinion.infinion.data.model.City
import com.infinion.infinion.databinding.FragmentFavouritesBinding
import com.infinion.infinion.db.entity.CityEntity
import com.infinion.infinion.ui.detail.WeatherDetailActivity
import com.infinion.infinion.ui.home.HomeViewModel
import com.infinion.infinion.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouritesFragment : Fragment() {

    private lateinit var binding: FragmentFavouritesBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var adapter: CitiesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavouritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Initialize Adapter
        adapter = CitiesAdapter(onCityClick = { city ->
            // Navigate to WeatherDetailActivity
            city?.let { res ->
                val desc = res.description ?: ""
                val temp = res.temperature.toString() ?: ""
                val i = Intent(requireContext(), WeatherDetailActivity::class.java).apply {
                    putExtra(Constants.EXTRA_CITY, res.name)
                    putExtra(Constants.EXTRA_DESC, desc)
                    putExtra(Constants.EXTRA_TEMP, temp)
                    putExtra(Constants.EXTRA_COUNTRY, res.country)
                    putExtra(Constants.EXTRA_FEEL_LIKE, res.feelsLike)
                    putExtra(Constants.EXTRA_LIKED, res.isSaved)
                    putExtra(Constants.EXTRA_ICON, res.icon)
                }
                startActivity(i)
            }
        }, onSaveClick = { city ->
            // Toggle saved state
            val cityModel = CityEntity(
                name = city.name,
                country = city.country,
                description = city.description,
                temperature = city.temperature,
                feelsLike = city.feelsLike,
                icon = city.icon,
            )
            viewModel.toggleCitySaved(city.name, cityModel)
        })


        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observe saved cities from DB
        viewModel.savedCities.observe(viewLifecycleOwner) { cityEntities ->
            val cities = cityEntities.map { entity ->
                City(
                    name = entity.name,
                    country = entity.country,
                    description = entity.description,
                    temperature = entity.temperature,
                    feelsLike = entity.feelsLike,
                    icon = entity.icon,
                    isSaved = true // since these are saved cities from DB
                )
            }
            adapter.submitList(cities)
        }

        // Local search filtering
        binding.searchBar.addTextChangedListener {
            adapter.filter.filter(it.toString())
        }

    }
}