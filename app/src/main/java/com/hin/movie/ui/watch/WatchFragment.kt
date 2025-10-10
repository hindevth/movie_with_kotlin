package com.hin.movie.ui.watch

import android.os.Bundle
import android.view.View
import androidx.annotation.OptIn
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
import timber.log.Timber


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
                exoPlayer.setMediaItem(
                    MediaItem.fromUri(
                        server?.firstOrNull()?.episodes?.firstOrNull()?.linkM3u8 ?: ""
                    )
                )
                exoPlayer.prepare()
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
    }

    private val videoListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            Timber.e("$playbackState 0000")
            val state = viewModel.videoState.value
            if (state?.timer?.value == state?.exoPlayer?.duration && playbackState == Player.STATE_ENDED) {
                binding.exoPlayer.keepScreenOn = false
                state?.exoPlayer?.pause()
                Timber.e("End")
            }
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
    override fun onResume() {
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
        viewModel.videoState.value?.exoPlayer?.removeListener(videoListener)
        viewModel.videoState.value?.exoPlayer?.release()
        viewModel.setExoPlayer(null)
        super.onDestroy()
    }
}