package com.codingpizza.euterpe.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingpizza.euterpe.model.DownloadStatus
import com.codingpizza.euterpe.model.ListItem
import com.codingpizza.repository.di.DownloadItemRepositoryImpl
import com.codingpizza.repository.di.ItemRepositoryImpl
import com.codingpizza.repository.open.DownloadedItemRepository
import com.codingpizza.repository.open.ItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    @ItemRepositoryImpl private val itemRepository: ItemsRepository,
    @DownloadItemRepositoryImpl private val downloadedItemRepository: DownloadedItemRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<DetailState> = MutableStateFlow(DetailState.Loading)
    val uiState: StateFlow<DetailState> = _uiState

    fun retrieveDetailId(id: String?) {
        if (id.isNullOrEmpty()) {
            _uiState.value = DetailState.Error("Item not found with id $id")
        } else {
            viewModelScope.launch {
                val result = itemRepository.getItemsById(id)
                result.fold(
                    ifLeft = {
                        _uiState.value = DetailState.Error("Item not found with id $id")
                    },
                    ifRight = { listItem ->
                        _uiState.value = DetailState.SuccessItemRetrieved(listItem)
                    }
                )
            }
        }
    }

    fun downloadAndPlay(listItem: ListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            downloadedItemRepository.downloadFile(listItem).collect { result ->
                result.fold(ifLeft = ::handleLeftStatus, ifRight = ::handleRightStatus)
            }
        }
    }

    private fun handleRightStatus(downloadStatus: DownloadStatus) {
        _uiState.value = when (downloadStatus) {
            is DownloadStatus.Finished -> DetailState.ItemDownloaded(downloadStatus.videoUri, downloadStatus.associatedListItem)
            is DownloadStatus.InProgress -> DetailState.DownloadItemProgress(
                downloadStatus.progress,
                downloadStatus.downloadRate,
                downloadStatus.associatedListItem.title,
                downloadStatus.associatedListItem.description
            )
            DownloadStatus.Paused -> DetailState.ItemDownloadPaused
        }
    }

    private fun handleLeftStatus(it: DownloadStatus.DownloadError) {
        _uiState.value = DetailState.Error(it.error)
    }
}
