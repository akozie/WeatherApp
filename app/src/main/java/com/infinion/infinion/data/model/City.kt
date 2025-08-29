package com.infinion.infinion.data.model

data class City(
    val name: String,
    val country: String,
    val description: String,
    val temperature: Float,
    val feelsLike: Float,
    val icon: String,
    val isSaved: Boolean = false
)
