package com.codingpizza.cache

import com.codingpizza.euterpe.model.ListItem
import com.codingpizza.euterpe.open.InsertItemDataSource
import javax.inject.Inject

class StoreItemDataSource @Inject constructor(
    private val database: AppDatabase
) : InsertItemDataSource {

    override suspend fun insertItems(items: List<ListItem>) {
        database.itemDao().insertAllItems(items.toItemCached())
    }
}
