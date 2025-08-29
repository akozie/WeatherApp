package com.infinion.infinion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.infinion.infinion.R
import com.infinion.infinion.data.model.City
import com.infinion.infinion.utils.LocalHelper.getWeatherIcon

class CitiesAdapter(
    private val onCityClick: (City) -> Unit,
    private val onSaveClick: (City) -> Unit
) : RecyclerView.Adapter<CitiesAdapter.CityViewHolder>(), Filterable {

    private var cityList = mutableListOf<City>()
    private var filteredList = mutableListOf<City>()

    fun submitList(list: List<City>) {
        cityList.clear()
        cityList.addAll(list)
        filteredList.clear()
        filteredList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_city, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(filteredList[position])
    }

    override fun getItemCount(): Int = filteredList.size

    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cityName: TextView = itemView.findViewById(R.id.cityName)
        private val description: TextView = itemView.findViewById(R.id.weatherDescription)
        private val descriptionIcon: ImageView = itemView.findViewById(R.id.weatherIcon)
        private val temperature: TextView = itemView.findViewById(R.id.temperature)
        private val countryName: TextView = itemView.findViewById(R.id.countryName)
        private val favIcon: ImageView = itemView.findViewById(R.id.favIcon)

        fun bind(city: City) {
            cityName.text = city.name
            countryName.text = city.country
            favIcon.setImageResource(
                if (city.isSaved) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
            )

            val desc = city.description ?: "N/A"
            val temp = city.temperature ?: "N/A"
            val feelsLike = city.feelsLike ?: "N/A"


            description.text = desc.replaceFirstChar { it.uppercase() }
            temperature.text = "$temp °C | feels like $feelsLike °C"

            city.icon.let { getWeatherIcon(it) }.let { it1 ->
                descriptionIcon.setImageResource(it1)
            }

            itemView.setOnClickListener { onCityClick(city) }
            favIcon.setOnClickListener { onSaveClick(city) }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase() ?: ""
                val results = if (query.isEmpty()) {
                    cityList
                } else {
                    cityList.filter {
                        it.name.lowercase().contains(query) ||
                        it.country.lowercase().contains(query)
                    }
                }
                return FilterResults().apply { values = results }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = (results?.values as? List<City>)?.toMutableList() ?: mutableListOf()
                notifyDataSetChanged()
            }
        }
    }
}
