package com.infinion.infinion.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val country: String,
    val description: String,
    val temperature: Float,
    val feelsLike: Float,
    val icon: String,
)
