package com.codingpizza.euterpe.ui.screens.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codingpizza.euterpe.ui.common.CenterBox
import com.codingpizza.euterpe.ui.common.CenterLoading
import com.codingpizza.euterpe.ui.common.ItemCard

const val LIST_SCREEN_ROUTE = "list"

@Composable
fun ListScreen(
    viewModel: ListScreenViewModel,
    onItemClick: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    when (state) {
        is ListState.Error -> {
            ListScreenScaffold {
                CenterBox {
                    Text(
                        "Error: ${(state as? ListState.Error)?.error}",
                        modifier = Modifier.clickable {
                            viewModel.retrieveData()
                        }
                    )
                }
            }
        }
        ListState.Loading -> {
            CenterLoading()
            viewModel.retrieveData()
        }
        is ListState.SuccessFullRetrieved -> {
            ListScreenScaffold(
                topbar = {
                    ListTopBar(
                        titleValue = (state as? ListState.SuccessFullRetrieved)?.searchBarText ?: "",
                        onSearch = {
                            val test = (state as? ListState.SuccessFullRetrieved)?.searchBarText ?: ""
                            viewModel.searchByQuery(test)
                        }
                    ) { textFieldValue ->
                        viewModel.setSearchBarText(textFieldValue)
                    }
                }
            ) {
                ItemList(state as ListState.SuccessFullRetrieved, onItemClick)
            }
        }
    }
}

@Composable
fun ListScreenScaffold(topbar: @Composable () -> Unit = {}, content: @Composable () -> Unit) {
    Scaffold(topBar = topbar) {
        content()
    }
}

@Composable
fun ListTopBar(titleValue: String, onSearch: () -> Unit, onTitleValueChange: (String) -> Unit) {
    val padding = 16.dp
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
            value = titleValue,
            onValueChange = onTitleValueChange,
            label = { Text("Title") },
            singleLine = true,
            keyboardActions = KeyboardActions { onSearch() }
        )
    }
}

@Composable
fun ItemList(successFullRetrieved: ListState.SuccessFullRetrieved, onItemClick: (Int) -> Unit) {
    Column {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(all = 16.dp)
        ) {
            items(successFullRetrieved.data,) { item -> ItemCard(item) { onItemClick(item.id) } }
        }
    }
}
