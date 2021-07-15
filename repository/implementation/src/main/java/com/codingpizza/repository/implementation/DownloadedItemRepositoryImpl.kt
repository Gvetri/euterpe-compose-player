package com.codingpizza.repository.implementation

import arrow.core.Either
import com.codingpizza.di.DownloadDataSources
import com.codingpizza.di.DownloadedDataSources
import com.codingpizza.di.StoredItemDataSources
import com.codingpizza.euterpe.model.DownloadStatus
import com.codingpizza.euterpe.model.ListItem
import com.codingpizza.euterpe.model.ListItemUriStatus
import com.codingpizza.euterpe.open.DownloadedDataSource
import com.codingpizza.euterpe.open.DownloadsDataSource
import com.codingpizza.euterpe.open.InsertItemDataSource
import com.codingpizza.repository.open.DownloadedItemRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class DownloadedItemRepositoryImpl @Inject constructor(
    @DownloadDataSources private val downloadDataSource: DownloadsDataSource,
    @DownloadedDataSources private val downloadedItemDataSource: DownloadedDataSource,
    @StoredItemDataSources private val storeItemDataSource: InsertItemDataSource,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : DownloadedItemRepository {

    override fun downloadFile(listItem: ListItem): Flow<Either<DownloadStatus.DownloadError, DownloadStatus>> = flow {
        val downloadProgress = downloadDataSource.downloadItem(listItem)
            .onEach { storeFile(it) }
        emitAll(downloadProgress)
    }.flowOn(defaultDispatcher)

    override fun retrieveDownloadedItems(): Flow<List<ListItem>> = downloadedItemDataSource.retrieveDownloadItems().flowOn(defaultDispatcher)

    private suspend fun storeFile(progress: Either<DownloadStatus.DownloadError, DownloadStatus>) {
        progress.fold(
            ifLeft = {},
            ifRight = { downloadStatus ->
                if (downloadStatus is DownloadStatus.Finished) {
                    downloadStatus.videoUri?.let { uri ->
                        storeItemDataSource.insertItems(listOf(downloadStatus.associatedListItem.copy(uriStatus = ListItemUriStatus.Stored(uri))))
                    }
                }
            }
        )
    }

}
