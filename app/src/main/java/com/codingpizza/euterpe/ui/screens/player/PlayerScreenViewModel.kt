package com.codingpizza.euterpe.ui.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingpizza.repository.di.ItemRepositoryImpl
import com.codingpizza.repository.open.ItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerScreenViewModel @Inject constructor(
    @ItemRepositoryImpl private val itemRepository: ItemsRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<PlayerScreenState> = MutableStateFlow(PlayerScreenState.Loading)
    val uiState: StateFlow<PlayerScreenState> = _uiState

    fun retrieveDetailId(id: String?) {
        if (id.isNullOrEmpty()) _uiState.value = PlayerScreenState.Error("An error has occurred, we couldn't find the item with that id.")
        else {
            viewModelScope.launch {
                val result = itemRepository.getItemsById(id)
                result.fold(
                    ifLeft = { _uiState.value = PlayerScreenState.Error("Item not found with id $id") },
                    ifRight = { listItem -> _uiState.value = PlayerScreenState.ItemRetrieved(listItem) }
                )
            }
        }
    }
}
