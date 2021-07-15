package com.codingpizza.remote

import arrow.core.Either
import com.codingpizza.euterpe.model.DownloadStatus
import com.codingpizza.euterpe.model.ListItem
import com.codingpizza.euterpe.open.DownloadsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DownloadDataSourceImpl @Inject constructor() : DownloadsDataSource {

    // Simulates an item download
    override fun downloadItem(listItem: ListItem): Flow<Either<DownloadStatus.DownloadError, DownloadStatus>> {
        val range = 1..10
        return range.asFlow().map { index ->
            kotlinx.coroutines.delay(1000)
            if (index == range.last) {
                Either.Right(
                    DownloadStatus.Finished(
                        associatedListItem = listItem,
                        videoUri = listItem.link
                    )
                )
            } else {
                Either.Right(
                    DownloadStatus.InProgress(
                        progress = (index * 0.1).toFloat(),
                        downloadRate = index,
                        associatedListItem = listItem
                    )
                )
            }
        }
    }
}
