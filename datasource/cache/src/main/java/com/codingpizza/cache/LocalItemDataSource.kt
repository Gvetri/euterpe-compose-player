package com.codingpizza.cache

import arrow.core.Either
import com.codingpizza.cachemodel.ItemCached
import com.codingpizza.euterpe.model.ListItem
import com.codingpizza.euterpe.model.ListItemUriStatus
import com.codingpizza.euterpe.open.RetrieveItemsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RetrieveCachedItemsDataSource @Inject constructor(
    private val database: AppDatabase
) : RetrieveItemsDataSource {

    override suspend fun retrieveItems(): List<ListItem> = database.itemDao().retrieveAllItems().map { itemsCached -> itemsCached.toItemList() }

    override fun observeItems(): Flow<List<ListItem>> = database.itemDao().observeAllItems().map { itemsCached -> itemsCached.toItemList() }

    override suspend fun retrieveItemsByQuery(query: String): Either<Error, List<ListItem>> = database.itemDao().getItemByQuery(query).run {
        Either.Right(this.toItemList())
    }

    override suspend fun retrieveItemById(id: String): Either<Unit, ListItem> = Either.fromNullable(database.itemDao().getItemById(id)?.toItemList())

}

fun List<ListItem>.toItemCached(): List<ItemCached> = map { listItem ->
    ItemCached(
        guid = listItem.guid,
        id = listItem.id,
        title = listItem.title,
        link = listItem.link,
        description = listItem.description,
        publicationDate = listItem.publicationDate,
        uriDownload = when (val uriStatus = listItem.uriStatus) {
            ListItemUriStatus.NotStored -> null
            is ListItemUriStatus.Stored -> uriStatus.uri
        }
    )
}

fun List<ItemCached>?.toItemList(): List<ListItem> = this?.map { itemCached ->
    ListItem(
        guid = itemCached.guid,
        title = itemCached.title,
        link = itemCached.link,
        description = itemCached.description,
        publicationDate = itemCached.publicationDate,
        uriStatus = if (itemCached.uriDownload.isNullOrEmpty()) ListItemUriStatus.NotStored else ListItemUriStatus.Stored(itemCached.uriDownload!!)
    )
} ?: emptyList()

private fun ItemCached.toItemList(): ListItem = ListItem(
    guid = guid,
    title = title,
    link = link,
    description = description,
    publicationDate = publicationDate,
    uriStatus = if (uriDownload.isNullOrEmpty()) ListItemUriStatus.NotStored else ListItemUriStatus.Stored(uriDownload!!)
)
