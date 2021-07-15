package com.codingpizza.euterpe.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.codingpizza.euterpe.R
import com.codingpizza.euterpe.ui.screens.download.DOWNLOAD_SCREEN_ROUTE
import com.codingpizza.euterpe.ui.screens.list.LIST_SCREEN_ROUTE

sealed class BottomBarScreens(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Feed : BottomBarScreens(LIST_SCREEN_ROUTE, R.string.feed, icon = Icons.Filled.List)
    object Downloads : BottomBarScreens(DOWNLOAD_SCREEN_ROUTE, R.string.downloads, icon = Icons.Filled.DownloadForOffline)
}
