package com.hin.movie.ui.watch.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hin.movie.data.entities.Server
import com.hin.movie.ui.watch.ServerFragment

class AdapterPaperServer(fm: Fragment, val servers: List<Server>) : FragmentStateAdapter(fm.childFragmentManager, fm.viewLifecycleOwner.lifecycle) {
    override fun createFragment(position: Int): Fragment {
        val episodes = servers[position].episodes
        return ServerFragment.newInstance(episodes, position)
    }

    override fun getItemCount(): Int  = servers.size
}