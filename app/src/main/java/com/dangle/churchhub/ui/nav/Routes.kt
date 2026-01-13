package com.dangle.churchhub.ui.nav

sealed class Route(val path: String) {
    data object Home : Route("home")
    data object Announcements : Route("announcements")
    data object ReadingPlan : Route("reading_plan")
    data object Sermons : Route("sermons")
}