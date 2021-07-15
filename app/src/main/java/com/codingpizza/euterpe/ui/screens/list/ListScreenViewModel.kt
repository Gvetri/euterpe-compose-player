package com.codingpizza.euterpe.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingpizza.repository.di.ItemRepositoryImpl
import com.codingpizza.repository.open.ItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListScreenViewModel @Inject constructor(@ItemRepositoryImpl private val itemRepository: ItemsRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<ListState> = MutableStateFlow(ListState.Loading)
    val uiState: StateFlow<ListState> = _uiState

    fun retrieveData() {
        viewModelScope.launch {
            itemRepository.getItems()
                .collect { list ->
                    _uiState.value = if (list.isEmpty()) ListState.Error("Empty List") else ListState.SuccessFullRetrieved(list)
                }
        }
    }

    fun setSearchBarText(title: String) {
        val previousState = _uiState.value as? ListState.SuccessFullRetrieved
        previousState?.let {
            _uiState.value = ListState.SuccessFullRetrieved(data = it.data, title)
        }
    }

    fun searchByQuery(searchBarText: String) {
        viewModelScope.launch {
            itemRepository.getItemsByQuery(searchBarText).collect { result ->
                result.fold(
                    ifLeft = {
                        _uiState.value = ListState.Error("Not items found")
                    },
                    ifRight = {
                        _uiState.value = ListState.SuccessFullRetrieved(it, searchBarText)
                    }
                )
            }
        }
    }
}
