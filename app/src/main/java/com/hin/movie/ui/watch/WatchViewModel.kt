package com.hin.movie.ui.watch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.hin.movie.data.entities.Episode
import com.hin.movie.data.entities.Movie
import com.hin.movie.data.entities.Server
import com.hin.movie.data.repository.HistoryRepository
import com.hin.movie.ui.base.BaseViewModel
import com.hin.movie.ui.custom.exo_player.data.Timer
import com.hin.movie.ui.watch.data.MovieState
import com.hin.movie.ui.watch.data.VideoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.Continuation

@HiltViewModel
class WatchViewModel @Inject constructor(private val historyRepository: HistoryRepository) :
    BaseViewModel() {

    private val _movieState = MutableLiveData<MovieState>(MovieState())
    val movieState: LiveData<MovieState> = _movieState

    private val _videoState = MutableLiveData<VideoState>(VideoState())
    val videoState: LiveData<VideoState> = _videoState

    private var jobHistory: Job? = null

    fun startProgressListener() {
        jobHistory?.cancel()
        jobHistory = viewModelScope.launch {
            while (true) {
                delay(5000L)
                if (videoState.value?.exoPlayer?.isPlaying == true) {
                    historyRepository.updatePosition(
                        movieState.value?.movie?.slug ?: "",
                        movieState.value?.currentEpisode?.slug ?: "",
                        videoState.value?.exoPlayer?.currentPosition ?: 0
                    )
                }
            }
        }
    }

    fun stopProgress() {
        jobHistory?.cancel()
    }

    fun setCurrentEpisode(episode: Episode, position: Int, positionServer: Int = 0) {
        _movieState.value =
            _movieState.value?.copy(currentEpisode = episode, currentPositionEpisode = position)
        changeVideo(episode.linkM3u8)

        viewModelScope.launch {
            val mv = movieState.value?.movie
            val sv = movieState.value?.servers?.get(positionServer)
            val ep = episode
            historyRepository.addHistory(mv, ep, 0, sv?.name ?: "")
        }
    }

    fun setServers(servers: List<Server>?) {
        _movieState.value = _movieState.value?.copy(servers = servers)
    }

    fun setMovie(movie: Movie?) {
        _movieState.value = _movieState.value?.copy(movie = movie)
    }

    fun setExoPlayer(player: ExoPlayer?) {
        _videoState.value = _videoState.value?.copy(exoPlayer = player)
        if (player != null) {
            initVideo()
        }
    }

    fun setTimer(timer: Timer) {
        _videoState.value = _videoState.value?.copy(timer = timer)
    }

    fun setPlaySpeed(speed: Float) {
        _videoState.value = _videoState.value?.copy(playbackSpeed = speed)
    }

    fun changeVideo(url: String?, seekTo: Long = 0) {
        val currentState = _videoState.value ?: return
        val player = currentState.exoPlayer ?: return
        val url = url ?: return
        player.stop()
        val mediaItem = MediaItem.fromUri(url)
        player.setMediaItem(mediaItem)
        player.seekTo(seekTo)
        player.prepare()
        player.play()
    }

    fun initVideo() {
        viewModelScope.launch {
            val history = historyRepository.getHistoryLast(movieState.value?.movie?.slug ?: "")
            if (history != null) {
                val index =
                    movieState.value?.servers?.first()?.episodes?.indexOfFirst { it.slug == history.slug }
                val indexServer =
                    movieState.value?.servers?.indexOfFirst { it.name == history.currentServerName }

                _movieState.value = _movieState.value?.copy(
                    currentEpisode = history.episode,
                    currentPositionEpisode = if (index != null && index >= 0) index else 0,
                    currentPositionServer = if (indexServer != null && indexServer >= 0) indexServer else 0
                )
                changeVideo(history.episode.linkM3u8, history.currentPositionEpisode)
            } else {
                val mv = movieState.value?.movie
                val sv = movieState.value?.servers?.first()
                val ep = sv?.episodes?.first()
                historyRepository.addHistory(mv, ep, 0, sv?.name ?: "")
                changeVideo(movieState.value?.servers?.first()?.episodes?.first()?.linkM3u8)
            }
            startProgressListener()
        }
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

    fun togglePlayAndPause() {
        if (videoState.value?.exoPlayer?.isPlaying == true) {
            videoState.value?.exoPlayer?.pause()
        } else {
            videoState.value?.exoPlayer?.play()
        }
    }

    fun cancelSleepTimer() {
        _videoState.value?.sleepJob?.cancel()
        _videoState.value = _videoState.value?.copy(sleepJob = null)
    }

    override fun onCleared() {
        stopProgress()
        _videoState.value?.exoPlayer?.release()
        _videoState.value = _videoState.value?.copy(exoPlayer = null)
        cancelSleepTimer()
        super.onCleared()
    }
}