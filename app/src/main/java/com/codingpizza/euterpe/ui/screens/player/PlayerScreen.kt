package com.codingpizza.euterpe.ui.screens.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.mediarouter.app.MediaRouteButton
import com.codingpizza.euterpe.R
import com.codingpizza.euterpe.extensions.obtainMimeTypeFromUri
import com.codingpizza.euterpe.model.ListItemUriStatus
import com.codingpizza.euterpe.ui.common.CenterLoading
import com.codingpizza.euterpe.ui.theme.EuterpeTheme
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.cast.CastPlayer
import com.google.android.exoplayer2.ext.cast.SessionAvailabilityListener
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastState

const val PLAYER_SCREEN_ROUTE = "player/{id}"
const val PLAYER_SCREEN_ROUTE_ID_PARAM_KEY = "id"

@Composable
fun PlayerScreen(viewModel: PlayerScreenViewModel, id: String) {
    val state by viewModel.uiState.collectAsState()
    viewModel.retrieveDetailId(id)
    Scaffold {
        when (state) {
            is PlayerScreenState.Error -> Text("An error has occurred ${(state as PlayerScreenState.Error).error}")
            is PlayerScreenState.ItemRetrieved -> PlayerScreenContainerBox(state as PlayerScreenState.ItemRetrieved)
            PlayerScreenState.Loading -> CenterLoading()
        }
    }
}

@Composable
private fun PlayerScreenContainerBox(itemRetrieved: PlayerScreenState.ItemRetrieved) {
    EuterpePlayer(
        uri = (itemRetrieved.item.uriStatus as ListItemUriStatus.Stored).uri,
        title = itemRetrieved.item.title
    )
}

@Composable
private fun PlayerTitle(title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.subtitle2,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun EuterpePlayer(uri: String, title: String) {
    var currentPosition by rememberSaveable { mutableStateOf(0L) }
    val castContext = generateRememberCastContext()
    generateCastPlayer(uri = uri, castContext = castContext)
    val player = generatePlayer(uri)
    val playerView = rememberPlayerViewWithLifecycle { currentPosition = it }
    BoxWithConstraints {
        if (minWidth < 600.dp) {
            Column {
                Box {
                    PlayerContainer(playerView, player, currentPosition, Modifier.fillMaxWidth())
                    CastButton(modifier = Modifier.align(Alignment.TopEnd))
                }
                PlayerTitle(title)
            }
        } else {
            Box {
                PlayerContainer(playerView, player, currentPosition, Modifier.fillMaxWidth().fillMaxHeight())
                CastButton(modifier = Modifier.align(Alignment.TopEnd))
            }
        }
    }
}

@Composable
private fun generateRememberCastContext(onCastSessionAvailable: (Boolean) -> Unit = {}): CastContext {
    val castContext: CastContext = CastContext.getSharedInstance(LocalContext.current)
    return remember {
        castContext.addCastStateListener { state ->
            if (state == CastState.NO_DEVICES_AVAILABLE) {
                onCastSessionAvailable(false)
            } else {
                onCastSessionAvailable(true)
            }
        }
        castContext
    }
}

@Composable
private fun CastButton(modifier: Modifier) {
    AndroidView(
        factory = { context ->
            MediaRouteButton(context).apply {
                CastButtonFactory.setUpMediaRouteButton(context, this)
            }
        },
        modifier = modifier.size(40.dp)
    )
}

@Composable
private fun generateCastPlayer(uri: String, castContext: CastContext, onDevicesAvailable: (Boolean) -> Unit = {}): CastPlayer {
    return remember {
        CastPlayer(castContext).apply {
            setSessionAvailabilityListener(object : SessionAvailabilityListener {
                override fun onCastSessionAvailable() {
                    onDevicesAvailable(true)
                    val mediaMetaData = MediaMetadata(MediaMetadata.MEDIA_TYPE_TV_SHOW)
                    mediaMetaData.putString(MediaMetadata.KEY_TITLE, "Title")
                    val mimeType = uri.obtainMimeTypeFromUri()
                    setMediaItem(
                        MediaItem.Builder()
                            .setUri(uri)
                            .setMimeType(mimeType)
                            .setMediaMetadata(mediaMetadata)
                            .build()
                    )
                    prepare()
                    playWhenReady = true
                }

                override fun onCastSessionUnavailable() {
                    onDevicesAvailable(false)
                }
            })
        }
    }
}

@Composable
private fun generatePlayer(uri: String): SimpleExoPlayer {
    val player = SimpleExoPlayer.Builder(LocalContext.current).build()
    val mediaItem = MediaItem.fromUri(uri)
    player.setMediaItem(mediaItem)
    player.prepare()
    player.play()
    return player
}

@Composable
private fun PlayerContainer(playerView: PlayerView, player: SimpleExoPlayer, currentPosition: Long, modifier: Modifier) {
    AndroidView(modifier = modifier, factory = { playerView }) { playerAndroidView ->
        playerAndroidView.player = player
        playerAndroidView.player?.seekTo(currentPosition)
    }
}

@Composable
private fun rememberPlayerViewWithLifecycle(onDisposeCalled: (Long) -> Unit): PlayerView {
    val context = LocalContext.current
    val playerView = remember {
        PlayerView(context).apply {
            id = R.id.playerview
            setShowNextButton(false)
            setShowPreviousButton(false)
        }
    }
    val lifecycleObserver = rememberPlayerLifecycleObserver(playerView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            val position = playerView.player?.currentPosition ?: 0
            onDisposeCalled(position)
            lifecycle.removeObserver(lifecycleObserver)
        }
    }
    return playerView
}

@Composable
private fun rememberPlayerLifecycleObserver(player: PlayerView): LifecycleEventObserver = remember(player) {
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> player.onResume()
            Lifecycle.Event.ON_PAUSE -> player.onPause()
            Lifecycle.Event.ON_DESTROY -> player.player?.release()
            else -> {
                // NOTHING TO DO HERE
            }
        }
    }
}

@Preview
@Composable
fun PlayerScreenPreview() {
    EuterpeTheme {
        EuterpePlayer(
            uri = "https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_16x9/bipbop_16x9_variant.m3u8",
            title = "This is a test title"
        )
    }
}
