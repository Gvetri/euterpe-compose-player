package com.codingpizza.euterpe.open

import com.codingpizza.euterpe.model.ListItem
import kotlinx.coroutines.flow.Flow

interface DownloadedDataSource {
    fun retrieveDownloadItems() : Flow<List<ListItem>>
}
