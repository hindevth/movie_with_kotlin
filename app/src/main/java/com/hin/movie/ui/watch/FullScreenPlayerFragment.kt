package com.hin.movie.ui.watch

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.annotation.OptIn
import androidx.fragment.app.activityViewModels
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.hin.movie.R
import com.hin.movie.databinding.FragmentFullScreenPlayerBinding
import com.hin.movie.ui.base.BaseFragment
import com.hin.movie.ui.custom.exo_player.ExoEventListener
import com.hin.movie.ui.custom.exo_player.data.Timer
import timber.log.Timber

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
    }

    private val videoListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            Timber.e("$playbackState 111")
            val state = viewModel.videoState.value
            if (state?.timer?.value == state?.exoPlayer?.duration && playbackState == Player.STATE_ENDED) {
                binding.exoPlayerVideo.keepScreenOn = false
                state?.exoPlayer?.pause()
                Timber.e("End")
            }
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
        viewModel.videoState.value?.exoPlayer.let { player ->
            PlayerView.switchTargetView(player!!, binding.exoPlayerVideo, null)
            player.removeListener(videoListener)
            binding.exoPlayerVideo.cleanup()
        }

        super.onDestroyView()
        // khôi phục orientation khi thoát
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FullScreenPlayerFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}