package com.codingpizza.repository.open

import arrow.core.Either
import com.codingpizza.euterpe.model.ListItem
import kotlinx.coroutines.flow.Flow

interface ItemsRepository {

    fun getItems(): Flow<List<ListItem>>

    fun getItemsByQuery(query: String): Flow<Either<Error, List<ListItem>>>

    suspend fun getItemsById(id: String): Either<Unit,ListItem>

}
