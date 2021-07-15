package com.codingpizza.euterpe.ui.screens.download

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingpizza.repository.di.DownloadItemRepositoryImpl
import com.codingpizza.repository.open.DownloadedItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    @DownloadItemRepositoryImpl private val downloadedItemRepository: DownloadedItemRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<DownloadUIState> = MutableStateFlow(DownloadUIState.Loading)
    val uiState: StateFlow<DownloadUIState> = _uiState

    fun retrieveDownloadItems() {
        viewModelScope.launch {
            downloadedItemRepository.retrieveDownloadedItems().collect { items ->
                _uiState.value = if (items.isEmpty()) DownloadUIState.SuccessWithEmptyList else DownloadUIState.Success(items)
            }
        }
    }
}
