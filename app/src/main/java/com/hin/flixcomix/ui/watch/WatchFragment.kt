package com.hin.flixcomix.ui.watch

import android.content.pm.ActivityInfo
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.hin.flixcomix.R
import com.hin.flixcomix.data.entities.Movie
import com.hin.flixcomix.data.entities.Server
import com.hin.flixcomix.databinding.FragmentWatchBinding
import com.hin.flixcomix.ui.base.BaseFragment
import com.hin.flixcomix.utils.extensions.getParcelableCompat
import com.hin.flixcomix.utils.extensions.getParcelableListCompat

class WatchFragment : BaseFragment<FragmentWatchBinding>(FragmentWatchBinding::inflate) {
    private val viewModel: WatchViewModel by viewModels()
    private var isFullScreen = false
    private var isMuted = false
    private lateinit var player: ExoPlayer
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movie = arguments?.getParcelableCompat<Movie>("movie")
        val server = arguments?.getParcelableListCompat<Server>("servers")
        player = ExoPlayer.Builder(requireContext()).build().also { exoPlayer ->
            binding.exoPlayer.player = exoPlayer
            exoPlayer.setMediaItem(
                MediaItem.fromUri("https://s6.kkphimplayer6.com/20250921/5o5dsiiN/index.m3u8")
            )
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
        }

        binding.exoPlayer.findViewById<TextView>(R.id.txtTitle).text = movie?.name
        binding.exoPlayer.findViewById<ImageButton>(R.id.btnVolume).setOnClickListener {
            player.volume = if (player.volume == 0f) 1f else 0f
            isMuted = !isMuted
            binding.exoPlayer.findViewById<ImageButton>(R.id.btnVolume).setImageResource(
                if (isMuted) R.drawable.volume_off else R.drawable.volume
            )
        }

        val fullscreenButton: ImageButton =
            binding.exoPlayer.findViewById(R.id.btn_fullscreen)

        fullscreenButton.setOnClickListener {
            if (isFullScreen) {
                exitFullScreen()
                fullscreenButton.setImageResource(R.drawable.fullscreen)
            } else {
                enterFullScreen()
                fullscreenButton.setImageResource(R.drawable.collapse)
            }
            isFullScreen = !isFullScreen
        }
    }

    private fun enterFullScreen() {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        requireActivity().window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    private fun exitFullScreen() {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }

//    override fun onResume() {
//        super.onResume()
//        player.play()
//    }


    override fun onDestroy() {
        player.release()
        super.onDestroy()
    }
}