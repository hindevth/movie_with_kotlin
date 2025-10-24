package com.hin.movie.ui.watch

import android.app.PendingIntent
import android.app.PictureInPictureParams
import android.app.RemoteAction
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.view.View
import android.widget.ImageButton
import androidx.annotation.OptIn
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import androidx.fragment.app.activityViewModels
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.hin.movie.R
import com.hin.movie.databinding.FragmentFullScreenPlayerBinding
import com.hin.movie.ui.base.BaseFragment
import com.hin.movie.ui.custom.exo_player.ExoEventListener
import com.hin.movie.ui.custom.exo_player.data.Timer
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

private const val ACTION_STOPWATCH_CONTROL = "stopwatch_control"
private const val EXTRA_CONTROL_TYPE = "control_type"
private const val REQUEST_START = 3
private const val REQUEST_PAUSE = 2

@AndroidEntryPoint
class FullScreenPlayerFragment() :
    BaseFragment<FragmentFullScreenPlayerBinding>(FragmentFullScreenPlayerBinding::inflate) {
    private val viewModel: WatchViewModel by activityViewModels()

    @OptIn(UnstableApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        requireActivity().window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        viewModel.videoState.observe(viewLifecycleOwner) { state ->
            binding.exoPlayerVideo.player?.addListener(videoListener)
            binding.exoPlayerVideo.setSleepEndTime(state.sleepEndTime)
            binding.exoPlayerVideo.setTimerSelected(state.timer)
            binding.exoPlayerVideo.setup(
                viewModel.videoState.value?.exoPlayer,
                parentFragmentManager
            )
            binding.exoPlayerVideo.findViewById<ImageButton>(R.id.btn_fullscreen)
                .setImageResource(R.drawable.collapse)
            binding.exoPlayerVideo.showEpisode()
            binding.exoPlayerVideo.setTitle(viewModel.movieState.value?.movie?.name!!)
            binding.exoPlayerVideo.setSpeed(viewModel.videoState.value?.playbackSpeed)
            binding.exoPlayerVideo.setExoEventListener(playerListener)
            if (binding.exoPlayerVideo.player == null && state.exoPlayer != null) {
                binding.exoPlayerVideo.setup(state.exoPlayer, parentFragmentManager)
            }
        }

        PlayerView.switchTargetView(
            viewModel.videoState.value?.exoPlayer!!,
            null,
            binding.exoPlayerVideo
        )


        registerReceiver(
            requireContext(), broadcastReceiver,
            IntentFilter(ACTION_STOPWATCH_CONTROL),
            ContextCompat.RECEIVER_VISIBLE_TO_INSTANT_APPS
        )
    }

    private val videoListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            val state = viewModel.videoState.value
            if (state?.timer?.value == state?.exoPlayer?.duration && playbackState == Player.STATE_ENDED) {
                binding.exoPlayerVideo.keepScreenOn = false
                state?.exoPlayer?.pause()
                Timber.e("End")
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            autoPip(started = isPlaying)
        }
    }

    private val playerListener = object : ExoEventListener {
        override fun onBack() {
            popNavigate()
        }

        override fun onFullScreen() {
            popNavigate()
        }

        override fun setSleepTimer(timer: Timer) {
            viewModel.setTimer(timer)
            viewModel.setSleepTimer(timer.value)
        }

        override fun setPlayBackSpeed(speed: Float) {
            viewModel.setPlaySpeed(speed)
        }
    }

    @OptIn(UnstableApi::class)
    override fun onDestroyView() {
        autoPip(false, started = false)
        viewModel.videoState.value?.exoPlayer.let { player ->
            PlayerView.switchTargetView(player!!, binding.exoPlayerVideo, null)
            player.removeListener(videoListener)
            binding.exoPlayerVideo.cleanup()
        }

        super.onDestroyView()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE

    }

    @OptIn(UnstableApi::class)
    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode)

        if (isInPictureInPictureMode) {
            binding.exoPlayerVideo.hideControls()
        } else {
            binding.exoPlayerVideo.showControls()
        }

    }

    override fun onPause() {
        enterPipMode()
        super.onPause()
    }

    override fun onResume() {
        autoPip()
        super.onResume()
    }

    fun getParamsPip(autoEnter: Boolean = true, started: Boolean = false): PictureInPictureParams {
        Timber.e("started $started")

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PictureInPictureParams.Builder()
                .setAspectRatio(Rational(16, 9))
                .setActions(listActionPip(started))
                .setAutoEnterEnabled(autoEnter)
                .setSeamlessResizeEnabled(false)
                .build()
        } else {
            PictureInPictureParams.Builder()
                .setAspectRatio(Rational(16, 9))
                .setActions(listActionPip(started))
                .build()
        }
    }

    fun listActionPip(started: Boolean = false) = listOf(
        if (!started) {
            RemoteAction(
                Icon.createWithResource(requireContext(), R.drawable.play2),
                resources.getString(R.string.play),
                resources.getString(R.string.play),
                PendingIntent.getBroadcast(
                    requireContext(),
                    REQUEST_START,
                    Intent(ACTION_STOPWATCH_CONTROL).putExtra(
                        EXTRA_CONTROL_TYPE,
                        REQUEST_START
                    ), PendingIntent.FLAG_IMMUTABLE
                )
            )
        } else {
            RemoteAction(
                Icon.createWithResource(requireContext(), R.drawable.pause),
                resources.getString(R.string.play),
                resources.getString(R.string.play),
                PendingIntent.getBroadcast(
                    requireContext(),
                    REQUEST_PAUSE,
                    Intent(ACTION_STOPWATCH_CONTROL).putExtra(
                        EXTRA_CONTROL_TYPE,
                        REQUEST_PAUSE
                    ), PendingIntent.FLAG_IMMUTABLE
                )
            )
        }

    )

    private val broadcastReceiver = object : BroadcastReceiver() {
        @OptIn(UnstableApi::class)
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null || intent.action != ACTION_STOPWATCH_CONTROL) {
                return
            }

            when (intent.getIntExtra(EXTRA_CONTROL_TYPE, 0)) {
                REQUEST_START -> {
                    binding.exoPlayerVideo.togglePlayAndPause()
                    autoPip(started = true)
                }

                REQUEST_PAUSE -> {
                    binding.exoPlayerVideo.togglePlayAndPause()
                    autoPip(started = false)
                }
            }
        }
    }

    fun autoPip(
        autoEnter: Boolean = true,
        started: Boolean = binding.exoPlayerVideo.player?.isPlaying == true
    ) {
        val params = getParamsPip(autoEnter, started)
        requireActivity().setPictureInPictureParams(params)
    }

    fun enterPipMode() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            val params = getParamsPip(started = binding.exoPlayerVideo.player?.isPlaying == true)
            requireActivity().enterPictureInPictureMode(params)
        }
    }
}