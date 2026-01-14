package com.dangle.churchhub.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.dangle.churchhub.ui.announcements.AnnouncementsScreen
import com.dangle.churchhub.ui.home.HomeScreen
import com.dangle.churchhub.ui.readingplan.ReadingPlanScreen
import com.dangle.churchhub.ui.sermons.SermonsScreen

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
        BottomItem(Route.ReadingPlan, "Plan", Icons.Filled.MenuBook),
        BottomItem(Route.Sermons, "Sermons", Icons.Filled.PlayCircle),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    val selected = currentRoute == item.route.path
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route.path) {
                                // avoids building a huge backstack when switching tabs
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
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
            composable(Route.Home.path) { HomeScreen() }
            composable(Route.Announcements.path) { AnnouncementsScreen() }
            composable(Route.ReadingPlan.path) { ReadingPlanScreen() }
            composable(Route.Sermons.path) { SermonsScreen() }
        }
    }
}
