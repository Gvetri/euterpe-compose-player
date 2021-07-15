package com.codingpizza.euterpe.model

sealed class DownloadStatus {
    data class InProgress(val progress: Float, val downloadRate: Int, val associatedListItem: ListItem) : DownloadStatus()
    data class Finished(val videoUri: String?, val associatedListItem: ListItem) : DownloadStatus()
    data class DownloadError(val error: String)
    object Paused : DownloadStatus()
}
