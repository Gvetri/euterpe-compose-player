package com.codingpizza.euterpe.open

import arrow.core.Either
import com.codingpizza.euterpe.model.DownloadStatus
import com.codingpizza.euterpe.model.ListItem
import kotlinx.coroutines.flow.Flow

interface DownloadsDataSource {

    fun downloadItem(listItem: ListItem): Flow<Either<DownloadStatus.DownloadError, DownloadStatus>>

}
