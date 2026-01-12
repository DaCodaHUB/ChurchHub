package com.dangle.churchhub.ui.nav

sealed class Route(val path: String) {
    data object Home : Route("home")
    data object Announcements : Route("announcements")
    data object Sermons : Route("sermons")
    data object More : Route("more")
}