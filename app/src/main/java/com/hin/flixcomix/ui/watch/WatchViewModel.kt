package com.hin.flixcomix.ui.watch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.hin.flixcomix.data.entities.Episode
import com.hin.flixcomix.data.entities.Movie
import com.hin.flixcomix.data.entities.Server
import com.hin.flixcomix.ui.base.BaseViewModel
import com.hin.flixcomix.ui.custom.exo_player.data.Timer
import com.hin.flixcomix.ui.watch.data.MovieState
import com.hin.flixcomix.ui.watch.data.VideoState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WatchViewModel : BaseViewModel() {

    private val _movieState = MutableLiveData<MovieState>(MovieState())
    val movieState: LiveData<MovieState> = _movieState

    private val _videoState = MutableLiveData<VideoState>(VideoState())
    val videoState: LiveData<VideoState> = _videoState

    fun setCurrentEpisode(episode: Episode, position: Int) {
        _movieState.value =
            _movieState.value?.copy(currentEpisode = episode, currentPositionEpisode = position)
        changeVideo(episode.linkM3u8)
    }

    fun setServers(servers: List<Server>?) {
        _movieState.value = _movieState.value?.copy(servers = servers)
    }

    fun setMovie(movie: Movie?) {
        _movieState.value = _movieState.value?.copy(movie = movie)
    }

    fun setExoPlayer(player: ExoPlayer?) {
        _videoState.value = _videoState.value?.copy(exoPlayer = player)
    }

    fun setTimer(timer: Timer) {
        _videoState.value = _videoState.value?.copy(timer = timer)
    }

    fun setPlaySpeed(speed: Float) {
        _videoState.value = _videoState.value?.copy(playbackSpeed = speed)
    }

    fun changeVideo(url: String?) {
        val currentState = _videoState.value ?: return
        val player = currentState.exoPlayer ?: return
        val url = url ?: return
        player.stop()
        val mediaItem = MediaItem.fromUri(url)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    fun setSleepTimer(time: Long) {
        val currentState = _videoState.value ?: return
        val player = currentState.exoPlayer ?: return

        if (time == 0L) {
            cancelSleepTimer()
            return
        }
        currentState.sleepJob?.cancel()

        val endTime = System.currentTimeMillis() + time
        _videoState.value = currentState.copy(sleepEndTime = endTime)

        val job = viewModelScope.launch {
            delay(time)
            player.pause()
            _videoState.value = _videoState.value?.copy(sleepEndTime = 0)
        }
        _videoState.value = _videoState.value?.copy(sleepJob = job)
    }

    fun cancelSleepTimer() {
        _videoState.value?.sleepJob?.cancel()
        _videoState.value = _videoState.value?.copy(sleepJob = null)
    }

    override fun onCleared() {
        _videoState.value?.exoPlayer?.release()
        _videoState.value = _videoState.value?.copy(exoPlayer = null)
        cancelSleepTimer()
        super.onCleared()
    }
}