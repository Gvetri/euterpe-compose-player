package com.codingpizza.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codingpizza.cachemodel.ItemCached
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM itemCached")
    fun observeAllItems(): Flow<List<ItemCached>>

    @Query("SELECT * FROM itemCached")
    fun retrieveAllItems(): List<ItemCached>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllItems(order: List<ItemCached?>)

    @Query("SELECT * FROM ItemCached WHERE id = :id")
    suspend fun getItemById(id: String): ItemCached?

    @Query("SELECT * FROM ItemCached WHERE uriDownload NOT NULL")
    fun getItemDownloaded(): Flow<List<ItemCached>>

    @Query("SELECT * FROM ItemCached WHERE title LIKE '%' || :title || '%'")
    suspend fun getItemByQuery(title: String): List<ItemCached>
}
