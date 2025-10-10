package com.hin.movie.ui.detail.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hin.movie.ui.comment.CommentTabFragment
import com.hin.movie.ui.grid_suggest.GridSuggestFragment

class AdapterPaperSuggest(fm: Fragment, val slugGenre: String?, val slugMovie: String?) : FragmentStateAdapter(fm.childFragmentManager, fm.viewLifecycleOwner.lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> GridSuggestFragment.newInstance(slugGenre, slugMovie)
            else -> CommentTabFragment()
        }
    }

    override fun getItemCount(): Int  = 2
}