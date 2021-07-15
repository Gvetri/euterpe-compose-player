package com.codingpizza.euterpe.ui.screens.list

import com.codingpizza.euterpe.model.ListItem

sealed class ListState {
    data class SuccessFullRetrieved(val data: List<ListItem>, val searchBarText: String = "") : ListState()
    object Loading : ListState()
    data class Error(val error: String, val searchBarText: String = "") : ListState()
}
