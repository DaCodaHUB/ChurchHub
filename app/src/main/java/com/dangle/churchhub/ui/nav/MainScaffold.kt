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
import com.dangle.churchhub.ui.announcements.detail.AnnouncementDetailScreen
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
                    val selected = currentRoute == item.route.path
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route.path) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
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

            composable(Route.Announcements.path) {
                AnnouncementsScreen(
                    onOpenAnnouncement = { id ->
                        navController.navigate(Route.AnnouncementDetail.create(id))
                    }
                )
            }

            // Detail route (required if you navigate to announcements/{id})
            composable(
                route = Route.AnnouncementDetail.path,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")!!
                AnnouncementDetailScreen(
                    announcementId = id,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Route.ReadingPlan.path) { ReadingPlanScreen() }
            composable(Route.Sermons.path) { SermonsScreen() }

            composable(Route.More.path) {
                MoreScreen(
                    onOpenSettings = { navController.navigate(Route.Settings.path) },
                    onOpenReadingPlan = { navController.navigate(Route.ReadingPlan.path) }
                )
            }

            composable(Route.Settings.path) { SettingsScreen() }
        }
    }
}
