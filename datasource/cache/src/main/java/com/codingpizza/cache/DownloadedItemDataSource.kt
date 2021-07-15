package com.codingpizza.cache

import com.codingpizza.euterpe.model.ListItem
import com.codingpizza.euterpe.open.DownloadedDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DownloadedItemDataSource @Inject constructor(private val database: AppDatabase) : DownloadedDataSource {

    override fun retrieveDownloadItems(): Flow<List<ListItem>> = database.itemDao().getItemDownloaded().map { it.toItemList() }

}
