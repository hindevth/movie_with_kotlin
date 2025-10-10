package com.hin.movie.ui.custom.exo_player

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.OptIn
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.TimeBar
import com.hin.movie.R
import com.hin.movie.databinding.CustomExoControlsBinding
import com.hin.movie.ui.custom.exo_player.bottom_sheet.BottomSheetPlayerSettings
import com.hin.movie.ui.custom.exo_player.bottom_sheet.PlayerSettingListener
import com.hin.movie.ui.custom.exo_player.data.Timer
import com.hin.movie.utils.extensions.gone
import com.hin.movie.utils.extensions.invisible
import com.hin.movie.utils.extensions.toTimeFormat
import com.hin.movie.utils.extensions.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.ref.WeakReference

@UnstableApi
class ExoPlayerVideo @OptIn(UnstableApi::class)
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    androidx.media3.ui.PlayerView(context, attrs), CoroutineScope by MainScope() {
    private val binding: CustomExoControlsBinding
    private var exoEventListener: ExoEventListener? = null
    private var sleepEndTime: Long? = null
    private var isReady = false
    private var isMuted = false
    private var speed: Float = 1f
    private var fragmentManager: FragmentManager? = null
    private var timer: Timer? = null

    private val hideHandler = Handler(Looper.getMainLooper())
    private val hideRunnable = Runnable {
        binding.root.invisible()
    }

    private var updateJob : Job? = null

    init {
        useController = false
        binding = CustomExoControlsBinding.inflate(LayoutInflater.from(context), this, true)
        setOnClickListener {
            if (binding.root.isVisible) {
                binding.root.invisible()
            } else {
                showControls()
            }
        }
        Timber.e("Init")
        onClick()
    }

    fun setup(player: Player?, fm: FragmentManager) {
        Timber.e("setup")
        this.player = player
        this.fragmentManager = fm
        this.player?.addListener(onListener)
        initVideo()
    }

    fun initVideo(){
        if (player == null) return
        binding.btnVolume.setImageResource(
            if (player?.volume == 0f) R.drawable.volume_off else R.drawable.volume
        )
        if (player?.isPlaying == true) {
            keepScreenOn = true
            binding.btnPlayPause.setImageResource(R.drawable.pause)
        } else {
            keepScreenOn = false
            binding.btnPlayPause.setImageResource(R.drawable.play2)
        }
        binding.exoProgress.addListener(onTimeBarListener)
        binding.exoProgress.showScrubber()
        binding.txtEndTime.text = player?.duration?.toTimeFormat()
        updateJob?.cancel()
        updateJob = launch {
            while (true){
                updateTimeBar()
                delay(1000)
            }
        }
    }
    fun cleanup() {
        player?.removeListener(onListener)
        player?.release()
        player = null

        updateJob?.cancel()
        updateJob = null
        cancel()

        hideHandler.removeCallbacksAndMessages(null)

        binding.exoProgress.removeListener(onTimeBarListener)

        fragmentManager = null
        exoEventListener = null
    }

    fun showControls() {
        binding.root.visible()
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, 3000)
    }

    @OptIn(UnstableApi::class)
    fun updateTimeBar(){
        binding.txtCurrentTime.text = player?.currentPosition?.toTimeFormat()
        binding.exoProgress.setBufferedPosition(player?.bufferedPosition ?: 0L)
        binding.exoProgress.setPosition(player?.currentPosition ?: 0L)
        binding.exoProgress.setDuration(player?.duration ?: 1L)
    }

    private val onTimeBarListener = object : TimeBar.OnScrubListener {
        override fun onScrubStart(timeBar: TimeBar, position: Long) {
        }

        override fun onScrubMove(timeBar: TimeBar, position: Long) {
        }

        override fun onScrubStop(
            timeBar: TimeBar,
            position: Long,
            canceled: Boolean
        ) {
            if (!canceled){
                player?.seekTo(position)
            }
        }

    }
    private val weakSelf = WeakReference(this)
    private val onListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            val self = weakSelf.get() ?: return
            Timber.e("playbackState $playbackState")
            if (playbackState == Player.STATE_READY) {
                Timber.e("STATE_READY1")
                self.isReady = true
                self.binding.txtEndTime.text = player?.duration?.toTimeFormat()
                self.updateJob?.cancel()
                self.updateJob = launch {
                    while (true){
                        updateTimeBar()
                        delay(1000)
                    }
                }
            }
            if (playbackState == Player.STATE_BUFFERING) {
                Timber.e("STATE_BUFFERING")
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            val self = weakSelf.get() ?: return
            if (isPlaying) {
                self.keepScreenOn = true
                self.binding.btnPlayPause.setImageResource(R.drawable.pause)
            } else {
                self.keepScreenOn = false
                self.binding.btnPlayPause.setImageResource(R.drawable.play2)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
//        cleanup()
    }

    private fun onClick() {
        binding.btnVolume.setOnClickListener {
            toggleMuted()
        }

        binding.btnMore.setOnClickListener {
            val remainingTime = getRemainingTime()
            fragmentManager?.let { fm ->
                BottomSheetPlayerSettings(player, timer, remainingTime, speed)
                    .setListener(bottomSettingListener)
                    .show(fm, "Settings")
            }

        }

        binding.btnForward.setOnClickListener {
            val newPosition = player!!.currentPosition + 10000
            val duration = player!!.duration

            if (duration != C.TIME_UNSET && newPosition > duration) {
                player?.seekTo(duration)
            } else {
                player?.seekTo(newPosition)
            }
        }

        binding.btnBackWard.setOnClickListener {
            val newPosition = (player!!.currentPosition - 10000).coerceAtLeast(0)
            player?.seekTo(newPosition)
        }

        binding.btnPlayPause.setOnClickListener {
            if (player?.isPlaying == true) {
                player?.pause()
                keepScreenOn = false
                binding.btnPlayPause.setImageResource(R.drawable.play2)
            } else {
                player?.play()
                keepScreenOn = true
                binding.btnPlayPause.setImageResource(R.drawable.pause)
            }
        }

        binding.btnBack.setOnClickListener {
            exoEventListener?.onBack()
        }

        binding.btnFullscreen.setOnClickListener {
            exoEventListener?.onFullScreen()
        }
    }
    fun toggleMuted(){
        player?.volume = if (player?.volume == 0f) 1f else 0f
        isMuted = !isMuted
        binding.btnVolume.setImageResource(
            if (isMuted) R.drawable.volume_off else R.drawable.volume
        )
    }
    private val bottomSettingListener = object : PlayerSettingListener {
        override fun onTimerSelected(timer: Timer) {
            this@ExoPlayerVideo.timer = timer
            exoEventListener?.setSleepTimer(timer)
        }

        override fun onSpeedChanged(speed: Float) {
            this@ExoPlayerVideo.speed = speed
            exoEventListener?.setPlayBackSpeed(speed)
        }
    }

    fun hideEpisode() {
        binding.btnEpisode.gone()
    }

    fun showEpisode() {
        binding.btnEpisode.visible()
    }

    fun setTitle(title: String) {
        binding.txtTitle.text = title
    }

    fun setExoEventListener(listener: ExoEventListener) {
        this.exoEventListener = listener
    }

    fun setSleepEndTime(time: Long) {
        sleepEndTime = time
    }

    fun setTimerSelected(timer: Timer?) {
        this.timer = timer
    }

    fun setSpeed(speed: Float?) {
        this.speed = speed ?: 1f
    }

    fun getRemainingTime(): Long {
        val end = sleepEndTime ?: return 0
        val remainingMillis = end - System.currentTimeMillis()
        return if (remainingMillis > 0) remainingMillis / 60000 else 0
    }


}

interface ExoEventListener {
    fun onBack()
    fun onFullScreen()
    fun setSleepTimer(timer: Timer)
    fun setPlayBackSpeed(speed: Float)
}