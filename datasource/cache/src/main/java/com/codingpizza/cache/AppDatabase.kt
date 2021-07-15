package com.codingpizza.cache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [com.codingpizza.cachemodel.ItemCached::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}
