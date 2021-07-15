package com.codingpizza.euterpe.model

sealed class ListItemUriStatus {
    data class Stored(val uri: String) : ListItemUriStatus()
    object NotStored : ListItemUriStatus()
}
