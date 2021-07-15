package com.codingpizza.repository.implementation

import arrow.core.Either
import com.codingpizza.di.RetrieveLocalDataSource
import com.codingpizza.di.RetrieveRemoteDataSource
import com.codingpizza.di.StoredItemDataSources
import com.codingpizza.euterpe.model.ListItem
import com.codingpizza.euterpe.open.InsertItemDataSource
import com.codingpizza.euterpe.open.RetrieveItemsDataSource
import com.codingpizza.repository.open.ItemsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ItemsRepositoryImpl @Inject constructor(
    @StoredItemDataSources private val storeItemDataSource: InsertItemDataSource,
    @RetrieveRemoteDataSource private val remoteDatasource: RetrieveItemsDataSource,
    @RetrieveLocalDataSource private val localDataSource: RetrieveItemsDataSource,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : ItemsRepository {

    override fun getItems(): Flow<List<ListItem>> = flow {
        val items = remoteDatasource.retrieveItems()
        storeItemDataSource.insertItems(items)
        emitAll(localDataSource.observeItems())
    }.flowOn(dispatcher)


    override fun getItemsByQuery(query: String): Flow<Either<Error, List<ListItem>>> = flow {
        val rssEither = remoteDatasource.retrieveItemsByQuery(query)
        rssEither.fold(
            ifRight = { items ->
                storeItemDataSource.insertItems(items)
                emit(localDataSource.retrieveItemsByQuery(query))
            },
            ifLeft = { emit(Either.Left(Error())) }
        )
    }.flowOn(dispatcher)

    override suspend fun getItemsById(id: String): Either<Unit, ListItem> = withContext(dispatcher){ localDataSource.retrieveItemById(id) }
}
