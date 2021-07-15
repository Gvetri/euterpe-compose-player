package com.codingpizza.euterpe.ui.screens.player

import com.codingpizza.euterpe.model.ListItem

sealed class PlayerScreenState {
    data class ItemRetrieved(val item: ListItem) : PlayerScreenState()
    object Loading : PlayerScreenState()
    data class Error(val error: String) : PlayerScreenState()
}
