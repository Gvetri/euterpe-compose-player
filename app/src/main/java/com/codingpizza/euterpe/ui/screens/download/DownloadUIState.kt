package com.codingpizza.euterpe.ui.screens.download

import com.codingpizza.euterpe.model.ListItem

sealed class DownloadUIState {
    data class Success(val items: List<ListItem>) : DownloadUIState()
    object Loading : DownloadUIState()
    data class Error(val error: String) : DownloadUIState()
    object SuccessWithEmptyList : DownloadUIState()
}
