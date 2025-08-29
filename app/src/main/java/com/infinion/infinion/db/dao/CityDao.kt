package com.infinion.infinion.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infinion.infinion.db.entity.CityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityEntity)

    @Query("SELECT * FROM cities ORDER BY id DESC")
    fun getAllCities(): Flow<List<CityEntity>>

    @Query("SELECT * FROM cities WHERE name = :name LIMIT 1")
    suspend fun getCityByName(name: String): CityEntity?

    @Delete
    suspend fun deleteCity(city: CityEntity)
}
