package com.codingpizza.euterpe.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Downloading
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.FileDownload
import androidx.compose.material.icons.rounded.FileDownloadOff
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codingpizza.euterpe.model.ListItem
import com.codingpizza.euterpe.model.ListItemUriStatus
import com.codingpizza.euterpe.ui.common.CenterLoading
import com.codingpizza.euterpe.ui.theme.EuterpeTheme
import com.codingpizza.euterpe.ui.theme.primaryVariantColor
import com.codingpizza.euterpe.ui.theme.secondaryColor
import timber.log.Timber

internal const val DETAIL_SCREEN_ROUTE = "detail/{id}"
internal const val DETAIL_SCREEN_ROUTE_ID_PARAM_KEY = "id"

@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    navController: NavController,
    onItemDownloaded: (Int) -> Unit
) {
    val id = navController.currentBackStackEntry?.arguments?.getString(DETAIL_SCREEN_ROUTE_ID_PARAM_KEY)
    val state by viewModel.uiState.collectAsState()
    viewModel.retrieveDetailId(id)
    Scaffold(
        floatingActionButton = {
            DetailFab(
                state,
                viewModel = viewModel,
                onItemDownloaded = onItemDownloaded
            )
        }
    ) {
        when (state) {
            is DetailState.Error -> Text("Error: ${(state as DetailState.Error).error}")
            DetailState.Loading -> CenterLoading()
            is DetailState.SuccessItemRetrieved -> {
                val successState = state as DetailState.SuccessItemRetrieved
                DetailContainer(
                    title = successState.item.title,
                    description = successState.item.description,
                    downloadStatus = successState.item.uriStatus
                )
            }
            is DetailState.ItemDownloaded -> {
                val progressState = state as DetailState.ItemDownloaded
                DetailContainer(
                    title = progressState.associatedListItem.title,
                    description = progressState.associatedListItem.description,
                    downloadStatus = progressState.associatedListItem.uriStatus
                )
            }
            DetailState.ItemDownloadPaused -> Text(text = "Download Paused")
            is DetailState.DownloadItemProgress -> {
                val progressState = state as DetailState.DownloadItemProgress
                DetailContainer(title = progressState.title, description = progressState.description, progress = progressState.progress)
            }
        }
    }
}

@Composable
private fun DetailFab(state: DetailState, viewModel: DetailViewModel, onItemDownloaded: (Int) -> Unit = {}) {
    when (state) {
        is DetailState.Error -> {
            DetailFabByState(icon = Icons.Rounded.Error, contentDescription = "Try again button") {
                Timber.d("Not available yet")
            }
        }
        DetailState.Loading -> Timber.d("Loading...")
        is DetailState.SuccessItemRetrieved -> ItemRetrieved(state, onItemDownloaded, viewModel)
        is DetailState.ItemDownloaded -> {
            DetailFabByState(icon = Icons.Rounded.PlayArrow, contentDescription = "Item download button") {
                onItemDownloaded(state.associatedListItem.id)
            }
        }
        DetailState.ItemDownloadPaused -> {
            DetailFabByState(icon = Icons.Rounded.FileDownloadOff, contentDescription = "Item paused button") {
                Timber.d("Not available yet")
            }
        }
        is DetailState.DownloadItemProgress -> {
            DetailFabByState(icon = Icons.Rounded.Downloading, contentDescription = "Download button in progress") {
                Timber.d("Not available yet")
            }
        }
    }
}

@Composable
private fun ItemRetrieved(state: DetailState.SuccessItemRetrieved, onItemDownloaded: (Int) -> Unit, viewModel: DetailViewModel) {
    val onClick = if (state.item.uriStatus is ListItemUriStatus.Stored) {
        { onItemDownloaded(state.item.id) }
    } else {
        { viewModel.downloadAndPlay(state.item) }
    }
    val icon = if (state.item.uriStatus is ListItemUriStatus.Stored) Icons.Rounded.PlayArrow else Icons.Rounded.FileDownload
    val contentDescription = if (state.item.uriStatus is ListItemUriStatus.Stored) "Move to player screen Button" else "Download Button"
    DetailFabByState(icon = icon, contentDescription = contentDescription, onClick = onClick)
}

@Composable
private fun DetailFabByState(icon: ImageVector, contentDescription: String, onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(icon, contentDescription = contentDescription)
    }
}

@Composable
private fun DetailContainer(
    title: String,
    description: String,
    progress: Float? = null,
    downloadStatus: ListItemUriStatus = ListItemUriStatus.NotStored
) {
    Column {
        DetailHeader(title)
        progress?.let { DetailDownloadProgress(progress = it) }
        DetailDescription(description)
        if (downloadStatus is ListItemUriStatus.Stored) {
            Text(text = "This item has been already downloaded", modifier = Modifier.padding(start = 16.dp, top = 16.dp))
        }
    }
}

@Composable
private fun DetailDownloadProgress(progress: Float = 1F, onFinishedColor: Color = primaryVariantColor, onProgressColor: Color = secondaryColor) {
    val color = if (progress == 1F) onFinishedColor else onProgressColor
    LinearProgressIndicator(
        progress = progress,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        color = color
    )
}

@Composable
private fun DetailDescription(description: String) {
    Text(
        text = description.trim(),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Justify,
    )
}

@Composable
private fun DetailHeader(name: String) {
    val maxHeightFraction = .3F
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(maxHeightFraction),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = name, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold
        )
    }
}
