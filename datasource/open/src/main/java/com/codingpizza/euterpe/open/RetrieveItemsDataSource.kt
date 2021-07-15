package com.codingpizza.euterpe.open

import arrow.core.Either
import com.codingpizza.euterpe.model.ListItem
import kotlinx.coroutines.flow.Flow

interface RetrieveItemsDataSource {

    suspend fun retrieveItems(): List<ListItem>

    fun observeItems(): Flow<List<ListItem>>

    suspend fun retrieveItemsByQuery(query: String): Either<Error, List<ListItem>>

    suspend fun retrieveItemById(id: String) : Either<Unit,ListItem>

}
