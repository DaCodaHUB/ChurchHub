package com.dangle.churchhub.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dangle.churchhub.ui.announcements.AnnouncementsScreen
import com.dangle.churchhub.ui.announcements.detail.AnnouncementDetailScreen
import com.dangle.churchhub.ui.home.HomeScreen
import com.dangle.churchhub.ui.readingplan.ReadingPlanScreen
import com.dangle.churchhub.ui.sermons.SermonsScreen

@Composable
fun AppNav() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Route.Home.path) {
        composable(Route.Home.path) { HomeScreen() }
        composable(Route.Announcements.path) {
            AnnouncementsScreen(onOpenAnnouncement = { id ->
                navController.navigate("announcements/$id")
            })
        }
        composable("announcements/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")!!
            AnnouncementDetailScreen(
                announcementId = id,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Route.ReadingPlan.path) { ReadingPlanScreen() }
        composable(Route.Sermons.path) { SermonsScreen() }
    }
}
