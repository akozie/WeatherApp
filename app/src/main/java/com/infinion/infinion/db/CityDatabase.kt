package com.infinion.infinion.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.infinion.infinion.db.dao.CityDao
import com.infinion.infinion.db.entity.CityEntity

@Database(entities = [CityEntity::class], version = 2, exportSchema = false)
abstract class CityDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
}
