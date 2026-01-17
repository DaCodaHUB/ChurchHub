package com.dangle.churchhub.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.dangle.churchhub.ui.announcements.AnnouncementsScreen
import com.dangle.churchhub.ui.home.HomeScreen
import com.dangle.churchhub.ui.more.MoreScreen
import com.dangle.churchhub.ui.readingplan.ReadingPlanScreen
import com.dangle.churchhub.ui.sermons.SermonsScreen
import com.dangle.churchhub.ui.settings.SettingsScreen

private data class BottomItem(
    val route: Route,
    val label: String,
    val icon: ImageVector
)

@Composable
fun MainScaffold() {
    val navController = rememberNavController()

    val items = listOf(
        BottomItem(Route.Home, "Home", Icons.Filled.Home),
        BottomItem(Route.Announcements, "News", Icons.Filled.Article),
        BottomItem(Route.Sermons, "Sermons", Icons.Filled.PlayCircle),
        BottomItem(Route.More, "More", Icons.Filled.MoreHoriz),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    val selected = when (item.route) {
                        Route.More -> currentRoute?.startsWith("more") == true
                        else -> currentRoute == item.route.path
                    }

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (item.route == Route.More) {
                                // If we're already somewhere under more/*, pop back to the More root screen.
                                val popped = navController.popBackStack(Route.More.path, inclusive = false)
                                if (!popped) {
                                    // If More isn't on the back stack yet, just navigate to it.
                                    navController.navigate(Route.More.path) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            } else {
                                navController.navigate(item.route.path) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Route.Home.path
        ) {
            composable(Route.Home.path) { HomeScreen(contentPadding = padding) }

            composable(Route.Announcements.path) { AnnouncementsScreen(contentPadding = padding) }

            composable(Route.Sermons.path) { SermonsScreen(contentPadding = padding) }

            composable(Route.More.path) {
                MoreScreen(
                    contentPadding = padding,
                    onOpenSettings = { navController.navigate(Route.MoreSettings.path) },
                    onOpenReadingPlan = { navController.navigate(Route.MoreReadingPlan.path) }
                )
            }

            // Nested More routes
            composable(Route.MoreSettings.path) { SettingsScreen(contentPadding = padding) }
            composable(Route.MoreReadingPlan.path) { ReadingPlanScreen(contentPadding = padding) }
        }
    }
}
