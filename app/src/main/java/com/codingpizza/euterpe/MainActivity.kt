package com.codingpizza.euterpe

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.codingpizza.euterpe.ui.navigation.BottomBarScreens
import com.codingpizza.euterpe.ui.navigation.Screens
import com.codingpizza.euterpe.ui.screens.detail.DETAIL_SCREEN_ROUTE
import com.codingpizza.euterpe.ui.screens.detail.DETAIL_SCREEN_ROUTE_ID_PARAM_KEY
import com.codingpizza.euterpe.ui.screens.detail.DetailScreen
import com.codingpizza.euterpe.ui.screens.detail.DetailViewModel
import com.codingpizza.euterpe.ui.screens.download.DOWNLOAD_SCREEN_ROUTE
import com.codingpizza.euterpe.ui.screens.download.DownloadScreen
import com.codingpizza.euterpe.ui.screens.download.DownloadViewModel
import com.codingpizza.euterpe.ui.screens.list.LIST_SCREEN_ROUTE
import com.codingpizza.euterpe.ui.screens.list.ListScreen
import com.codingpizza.euterpe.ui.screens.list.ListScreenViewModel
import com.codingpizza.euterpe.ui.screens.player.PLAYER_SCREEN_ROUTE
import com.codingpizza.euterpe.ui.screens.player.PLAYER_SCREEN_ROUTE_ID_PARAM_KEY
import com.codingpizza.euterpe.ui.screens.player.PlayerScreen
import com.codingpizza.euterpe.ui.screens.player.PlayerScreenViewModel
import com.codingpizza.euterpe.ui.theme.EuterpeTheme
import com.codingpizza.euterpe.ui.theme.backgroundColor
import com.codingpizza.euterpe.ui.theme.onSurfaceColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EuterpeTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                val items = listOf(BottomBarScreens.Feed, BottomBarScreens.Downloads)
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(
                        bottomBar = {
                            BottomNavigation(backgroundColor = backgroundColor, contentColor = onSurfaceColor) {
                                val navBackStackEntry by navController.currentBackStackEntryAsState()
                                val currentDestination = navBackStackEntry?.destination
                                items.forEach { screen ->
                                    BottomNavigationItem(
                                        icon = { Icon(screen.icon, contentDescription = null) },
                                        label = { Text(text = stringResource(id = screen.resourceId)) },
                                        selected = currentDestination?.hierarchy?.any { destination -> destination.route == screen.route } == true,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                    )
                                }
                            }
                        }
                    ) {
                        NavHost(navController = navController, startDestination = LIST_SCREEN_ROUTE, modifier = Modifier.padding(it)) {
                            addFeedScreen(navController)
                            addDownloadScreen(navController)
                            addDetailGraph(navController)
                            addPlayerGraph()
                        }
                    }
                }
            }
        }
    }
}

private fun NavGraphBuilder.addFeedScreen(navController: NavController) {
    composable(LIST_SCREEN_ROUTE) {
        val viewModel: ListScreenViewModel = hiltViewModel()
        ListScreen(viewModel = viewModel) { itemId ->
            val route = Screens.ItemDetail.generateRoute(DETAIL_SCREEN_ROUTE, DETAIL_SCREEN_ROUTE_ID_PARAM_KEY, itemId)
            navController.navigate(route)
        }
    }
}

private fun NavGraphBuilder.addDownloadScreen(navController: NavController) {
    composable(DOWNLOAD_SCREEN_ROUTE) {
        val viewModel: DownloadViewModel = hiltViewModel()
        DownloadScreen(viewModel = viewModel) { listItem ->
            val route = Screens.ItemDetail.generateRoute(DETAIL_SCREEN_ROUTE, DETAIL_SCREEN_ROUTE_ID_PARAM_KEY, listItem.id)
            navController.navigate(route)
        }
    }
}

private fun NavGraphBuilder.addPlayerGraph() {
    composable(PLAYER_SCREEN_ROUTE) {
        val viewModel: PlayerScreenViewModel = hiltViewModel()
        val itemId = it.arguments?.getString(PLAYER_SCREEN_ROUTE_ID_PARAM_KEY)
        requireNotNull(itemId) { "$PLAYER_SCREEN_ROUTE_ID_PARAM_KEY wasn't found." }
        PlayerScreen(viewModel = viewModel, id = itemId)
    }
}

private fun NavGraphBuilder.addDetailGraph(navController: NavController) {
    composable(DETAIL_SCREEN_ROUTE) {
        val itemDetailId = it.arguments?.getString(DETAIL_SCREEN_ROUTE_ID_PARAM_KEY)
        requireNotNull(itemDetailId) { "$DETAIL_SCREEN_ROUTE_ID_PARAM_KEY wasn't found." }
        val viewModel: DetailViewModel = hiltViewModel()
        DetailScreen(
            viewModel = viewModel,
            navController = navController,
            onItemDownloaded = { itemDownloadedId ->
                val route = Screens.Player.generateRoute(PLAYER_SCREEN_ROUTE, PLAYER_SCREEN_ROUTE_ID_PARAM_KEY, itemDownloadedId)
                navController.navigate(route)
            }
        )
    }
}
