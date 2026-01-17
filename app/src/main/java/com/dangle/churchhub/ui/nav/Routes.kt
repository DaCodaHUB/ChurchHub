package com.dangle.churchhub.ui.nav

sealed class Route(val path: String) {
    data object Home : Route("home")
    data object Announcements : Route("announcements")
    data object ReadingPlan : Route("reading_plan") // keep as standalone if you want
    data object Sermons : Route("sermons")

    data object More : Route("more")

    // Nested routes under More
    data object MoreSettings : Route("more/settings")
    data object MoreReadingPlan : Route("more/reading_plan")
}
