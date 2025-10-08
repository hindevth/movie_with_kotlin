package com.hin.flixcomix.ui.watch

import android.os.Bundle
import android.view.View
import com.hin.flixcomix.R
import com.hin.flixcomix.data.entities.Episode
import com.hin.flixcomix.databinding.FragmentServerBinding
import com.hin.flixcomix.ui.base.BaseFragment
import com.hin.flixcomix.ui.base.GridSpacingItemDecoration
import com.hin.flixcomix.ui.watch.adapter.AdapterEpisode
import com.hin.flixcomix.utils.extensions.getParcelableListCompat
import com.hin.flixcomix.utils.extensions.setupFlexbox
import timber.log.Timber

private const val ARG_EPISODES = "episodes"

class ServerFragment : BaseFragment<FragmentServerBinding>(FragmentServerBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val episodes = arguments?.getParcelableListCompat<Episode>(ARG_EPISODES)
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
    }

    companion object {

        @JvmStatic
        fun newInstance(episode: List<Episode>?) =
            ServerFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_EPISODES, ArrayList(episode))
                }
            }
    }
}