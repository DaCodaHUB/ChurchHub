package com.dangle.churchhub.core.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

fun openYouTubeVideo(context: Context, videoId: String) {
    val appUri = Uri.parse("vnd.youtube:$videoId")
    val webUri = Uri.parse("https://www.youtube.com/watch?v=$videoId")

    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, appUri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    } catch (_: ActivityNotFoundException) {
        context.startActivity(Intent(Intent.ACTION_VIEW, webUri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}
