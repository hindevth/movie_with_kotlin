package com.hin.flixcomix.ui.watch

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.hin.flixcomix.R
import com.hin.flixcomix.data.entities.Episode
import com.hin.flixcomix.databinding.FragmentServerBinding
import com.hin.flixcomix.ui.base.BaseFragment
import com.hin.flixcomix.ui.base.GridSpacingItemDecoration
import com.hin.flixcomix.ui.watch.adapter.AdapterEpisode
import com.hin.flixcomix.ui.watch.adapter.EpisodeListener
import com.hin.flixcomix.utils.extensions.getParcelableListCompat
import com.hin.flixcomix.utils.extensions.setupFlexbox
import timber.log.Timber
import kotlin.getValue

private const val ARG_EPISODES = "episodes"
private const val ARG_POSITION = "position"

class ServerFragment : BaseFragment<FragmentServerBinding>(FragmentServerBinding::inflate) {
    private val viewModel: WatchViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val episodes = arguments?.getParcelableListCompat<Episode>(ARG_EPISODES)
        val position = arguments?.getInt(ARG_POSITION)
        val adapterEpisode = AdapterEpisode()
        binding.recyclerView.adapter = adapterEpisode
        val spacingItemGrid = resources.getDimensionPixelSize(R.dimen.item_grid_spacing)
        binding.recyclerView.addItemDecoration(
            GridSpacingItemDecoration(
                4,
                spacingItemGrid,
                true,
                0
            )
        )
        adapterEpisode.submitList(episodes)
        adapterEpisode.setListener(episodeListener)
        if (position == 0) {
            adapterEpisode.setSelectedItem(0)
        }
    }

    private val episodeListener = object : EpisodeListener {
        override fun onClick(item: Episode, position: Int) {
            viewModel.setCurrentEpisode(item, position)
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(episode: List<Episode>?, position: Int) =
            ServerFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_EPISODES, ArrayList(episode ?: emptyList()))
                    putInt(ARG_POSITION, position)
                }
            }
    }
}