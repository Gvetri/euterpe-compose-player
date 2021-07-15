package com.codingpizza.euterpe.ui.screens.download

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codingpizza.euterpe.model.ListItem
import com.codingpizza.euterpe.ui.common.CenterLoading
import com.codingpizza.euterpe.ui.common.ItemCard

const val DOWNLOAD_SCREEN_ROUTE = "download"

@Composable
fun DownloadScreen(viewModel: DownloadViewModel, onItemClicked: (ListItem) -> Unit) {
    val state by viewModel.uiState.collectAsState()
    viewModel.retrieveDownloadItems()
    DownloadScaffold(state, onItemClicked = onItemClicked)
}

@Composable
private fun DownloadScaffold(state: DownloadUIState, onItemClicked: (ListItem) -> Unit) {
    when (state) {
        is DownloadUIState.Error -> Text(text = "An error has ocurred")
        DownloadUIState.Loading -> CenterLoading()
        is DownloadUIState.Success -> DownloadList(state, onItemClicked)
        DownloadUIState.SuccessWithEmptyList -> Text(text = "There is not item downloaded yet")
    }
}

@Composable
private fun DownloadList(state: DownloadUIState.Success, onItemClicked: (ListItem) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(all = 16.dp)) {
        items(state.items) { item -> ItemCard(item) { onItemClicked(item) } }
    }
}

@Preview
@Composable
fun DownloadScaffoldPreview() {
    val listItem = ListItem(
        "1",
        "Dummy Video",
        "dummyLink",
        "dummy description",
        "abc123"
    )
    val listItem2 = ListItem(
        "1",
        "Dummy Video",
        "dummyLink",
        "dummy description",
        "abc123"
    )
    val list = listOf(listItem, listItem2)
    val state = DownloadUIState.Success(list)
    DownloadScaffold(state) {}
}
