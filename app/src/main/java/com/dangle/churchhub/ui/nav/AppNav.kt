package com.dangle.churchhub.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dangle.churchhub.ui.announcements.AnnouncementsScreen
import com.dangle.churchhub.ui.home.HomeScreen
import com.dangle.churchhub.ui.more.MoreScreen
import com.dangle.churchhub.ui.sermons.SermonsScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.dangle.churchhub.ui.sermons.detail.SermonDetailScreen

@Composable
fun AppNav() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Home.path
    ) {
        composable(Route.Home.path) { HomeScreen() }
        composable(Route.Announcements.path) { AnnouncementsScreen() }
        composable(Route.Sermons.path) {
            SermonsScreen(onSermonClick = { id ->
                navController.navigate(Route.SermonDetail.create(id))
            })
        }
        composable(Route.More.path) { MoreScreen() }
        composable(
            route = Route.SermonDetail.path,
            arguments = listOf(navArgument("sermonId") { type = NavType.StringType })
        ) { backStackEntry ->
            val sermonId = backStackEntry.arguments?.getString("sermonId")!!
            SermonDetailScreen(
                sermonId = sermonId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}