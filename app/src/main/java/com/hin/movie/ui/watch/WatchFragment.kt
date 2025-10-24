package com.hin.movie.ui.watch

import android.app.PendingIntent
import android.app.PictureInPictureParams
import android.app.RemoteAction
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.view.View
import androidx.annotation.OptIn
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import androidx.fragment.app.activityViewModels
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.google.android.material.tabs.TabLayoutMediator
import com.hin.movie.R
import com.hin.movie.data.entities.Movie
import com.hin.movie.data.entities.Server
import com.hin.movie.databinding.FragmentWatchBinding
import com.hin.movie.ui.base.BaseFragment
import com.hin.movie.ui.custom.exo_player.ExoEventListener
import com.hin.movie.ui.custom.exo_player.data.Timer
import com.hin.movie.ui.watch.adapter.AdapterPaperServer
import com.hin.movie.utils.extensions.getParcelableCompat
import com.hin.movie.utils.extensions.getParcelableListCompat
import com.hin.movie.utils.extensions.gone
import com.hin.movie.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

private const val ACTION_STOPWATCH_CONTROL = "stopwatch_control"
private const val EXTRA_CONTROL_TYPE = "control_type"
private const val REQUEST_START = 1
private const val REQUEST_PAUSE = 0

@AndroidEntryPoint
class WatchFragment : BaseFragment<FragmentWatchBinding>(FragmentWatchBinding::inflate) {
    private val viewModel: WatchViewModel by activityViewModels()
    private var tabLayoutMediator: TabLayoutMediator? = null

    @OptIn(UnstableApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val movie = arguments?.getParcelableCompat<Movie>("movie")
        val server = arguments?.getParcelableListCompat<Server>("servers")

        viewModel.setMovie(movie)
        viewModel.setServers(server)

        if (viewModel.videoState.value?.exoPlayer == null) {
            val player = ExoPlayer.Builder(requireContext()).build().also { exoPlayer ->
                binding.exoPlayer.player = exoPlayer
            }
//            player.addListener(videoListener)
            binding.exoPlayer.setup(player, parentFragmentManager)
            viewModel.setExoPlayer(player)
        } else {
            binding.exoPlayer.setup(viewModel.videoState.value?.exoPlayer, parentFragmentManager)
        }

        viewModel.videoState.observe(viewLifecycleOwner) { state ->
            binding.exoPlayer.setSleepEndTime(state.sleepEndTime)
            binding.exoPlayer.setTimerSelected(state.timer)
            binding.exoPlayer.player?.addListener(videoListener)
        }

        viewModel.movieState.observe(viewLifecycleOwner) {state ->
            if (state.currentPositionServer != null && binding.viewPaper.currentItem != state.currentPositionServer){
                binding.viewPaper.currentItem = state.currentPositionServer
            }

        }
        binding.exoPlayer.setTitle(movie?.name!!)
        onClick()
        init()
    }

    private fun init() {
        val movie = arguments?.getParcelableCompat<Movie>("movie")
        val servers = arguments?.getParcelableListCompat<Server>("servers")

        if (tabLayoutMediator == null && servers != null) {
            binding.viewPaper.adapter =
                AdapterPaperServer(this, servers)
            tabLayoutMediator =
                TabLayoutMediator(binding.tabLayout, binding.viewPaper) { tab, position ->
                    tab.text = servers[position].name
                }.apply { attach() }

        }
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
                binding.exoPlayer.keepScreenOn = false
                state?.exoPlayer?.pause()
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            autoPip(started = isPlaying)
        }
    }

    @OptIn(UnstableApi::class)
    fun onClick() {
        binding.exoPlayer.setExoEventListener(object : ExoEventListener {
            override fun onBack() {
                popNavigate()
            }

            override fun onFullScreen() {
                PlayerView.switchTargetView(
                    viewModel.videoState.value?.exoPlayer!!,
                    binding.exoPlayer,
                    null
                )
                navigateTo(R.id.action_watchFullScreenFragment)
            }

            override fun setSleepTimer(timer: Timer) {
                viewModel.setSleepTimer(timer.value)
                viewModel.setTimer(timer)
            }

            override fun setPlayBackSpeed(speed: Float) {
                viewModel.setPlaySpeed(speed)
            }

        })
    }

    @OptIn(UnstableApi::class)
    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode)

        if (isInPictureInPictureMode) {
            binding.viewPaper.gone()
            binding.tabLayout.gone()
            binding.layoutComment.root.gone()
            binding.exoPlayer.hideControls()
        } else {
            binding.viewPaper.visible()
            binding.tabLayout.visible()
            binding.layoutComment.root.visible()
            binding.exoPlayer.showControls()
        }

    }

    fun getParamsPip(autoEnter: Boolean = true, started: Boolean = false): PictureInPictureParams {
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
                    viewModel.togglePlayAndPause()
//                    binding.exoPlayer.togglePlayAndPause()
                    autoPip(started = true)
                }
                REQUEST_PAUSE -> {
                    viewModel.togglePlayAndPause()
//                    binding.exoPlayer.togglePlayAndPause()
                    autoPip(started = false)
                }
            }
        }
    }

    fun autoPip(
        autoEnter: Boolean = true,
        started: Boolean = binding.exoPlayer.player?.isPlaying == true
    ) {
        val params = getParamsPip(autoEnter, started)
        requireActivity().setPictureInPictureParams(params)
    }

    fun enterPipMode() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            val params = getParamsPip(started = binding.exoPlayer.player?.isPlaying == true)
            requireActivity().enterPictureInPictureMode(params)
        }
    }

    override fun onPause() {
        enterPipMode()
        super.onPause()
    }

    @OptIn(UnstableApi::class)
    override fun onResume() {
        autoPip()
        super.onResume()
        viewModel.videoState.value?.exoPlayer?.let { player ->
            PlayerView.switchTargetView(player, null, binding.exoPlayer)
        }
    }

    @OptIn(UnstableApi::class)
    override fun onDestroyView() {
        binding.exoPlayer.cleanup()
        viewModel.videoState.value?.exoPlayer?.removeListener(videoListener)
        tabLayoutMediator?.detach()
        tabLayoutMediator = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        autoPip(false, started = false)
        viewModel.videoState.value?.exoPlayer?.removeListener(videoListener)
        viewModel.videoState.value?.exoPlayer?.release()
        viewModel.setExoPlayer(null)
        super.onDestroy()
    }
}