package com.hin.movie.ui.watch.data

import androidx.media3.exoplayer.ExoPlayer
import com.hin.movie.ui.custom.exo_player.data.Timer
import kotlinx.coroutines.Job

data class VideoState(
    val exoPlayer: ExoPlayer? = null,
    val sleepJob: Job? = null,
    val sleepEndTime: Long = 0L,
    val timer: Timer? = null,
    val playbackSpeed: Float = 1.0F,
)
