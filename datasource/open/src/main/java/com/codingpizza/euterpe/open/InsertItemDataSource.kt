package com.codingpizza.euterpe.open

import com.codingpizza.euterpe.model.ListItem

interface InsertItemDataSource {

    suspend fun insertItems(items: List<ListItem>)

}
