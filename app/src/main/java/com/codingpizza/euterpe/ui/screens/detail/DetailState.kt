package com.codingpizza.euterpe.ui.screens.detail

import com.codingpizza.euterpe.model.ListItem

sealed class DetailState {
    data class SuccessItemRetrieved(val item: ListItem) : DetailState()
    object Loading : DetailState()
    data class Error(val error: String) : DetailState()
    data class ItemDownloaded(val uri: String?, val associatedListItem: ListItem) : DetailState()
    object ItemDownloadPaused : DetailState()
    data class DownloadItemProgress(val progress: Float, val downloadRate: Int, val title: String, val description: String) :
        DetailState()
}
