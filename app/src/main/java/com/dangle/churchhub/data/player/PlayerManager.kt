package com.dangle.churchhub.data.player

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.dangle.churchhub.domain.model.PlayerState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val player: ExoPlayer = ExoPlayer.Builder(context).build()

    private val _state = MutableStateFlow(PlayerState())
    val state: StateFlow<PlayerState> = _state

    private var currentMediaId: String? = null

    private val listener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            pushState()
        }
        override fun onPlaybackStateChanged(playbackState: Int) {
            pushState()
        }
    }

    init {
        player.addListener(listener)
    }

    fun play(mediaId: String, url: String) {
        // If a new sermon, load it; if same, just resume
        if (currentMediaId != mediaId) {
            currentMediaId = mediaId
            val item = MediaItem.Builder()
                .setMediaId(mediaId)
                .setUri(url)
                .build()
            player.setMediaItem(item)
            player.prepare()
        }
        player.play()
        pushState()
    }

    fun playResolved(mediaId: String, source: String) {
        // source can be "https://..." OR "file://..." OR "content://..."
        if (currentMediaId != mediaId) {
            currentMediaId = mediaId
            val item = MediaItem.Builder()
                .setMediaId(mediaId)
                .setUri(Uri.parse(source))
                .build()
            player.setMediaItem(item)
            player.prepare()
        }
        player.play()
        pushState()
    }

    fun pause() {
        player.pause()
        pushState()
    }

    fun toggle(mediaId: String, source: String) {
        if (currentMediaId == mediaId && player.isPlaying) pause()
        else playResolved(mediaId = mediaId, source = source)
    }
    fun seekTo(positionMs: Long) {
        player.seekTo(positionMs)
        pushState()
    }

    fun release() {
        player.removeListener(listener)
        player.release()
    }

    private fun pushState() {
        _state.value = PlayerState(
            isPlaying = player.isPlaying,
            positionMs = player.currentPosition.coerceAtLeast(0L),
            durationMs = player.duration.coerceAtLeast(0L)
        )
    }
}
