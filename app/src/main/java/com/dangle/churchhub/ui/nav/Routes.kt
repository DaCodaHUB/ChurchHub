package com.dangle.churchhub.ui.nav

sealed class Route(val path: String) {
    data object Home : Route("home")
    data object Announcements : Route("announcements")
    data object ReadingPlan : Route("reading_plan")
    data object Sermons : Route("sermons")

    data object More : Route("more")
    data object Settings : Route("settings")

    // Announcements detail (if you already added it)
    data object AnnouncementDetail : Route("announcements/{id}") {
        fun create(id: String) = "announcements/$id"
    }
}
