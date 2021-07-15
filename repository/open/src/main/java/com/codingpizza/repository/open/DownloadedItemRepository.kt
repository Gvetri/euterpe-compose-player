package com.codingpizza.repository.open

import arrow.core.Either
import com.codingpizza.euterpe.model.DownloadStatus
import com.codingpizza.euterpe.model.ListItem
import kotlinx.coroutines.flow.Flow

interface DownloadedItemRepository {

    fun downloadFile(listItem: ListItem): Flow<Either<DownloadStatus.DownloadError, DownloadStatus>>

    fun retrieveDownloadedItems(): Flow<List<ListItem>>
}
