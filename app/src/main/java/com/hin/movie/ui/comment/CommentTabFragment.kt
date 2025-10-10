package com.hin.movie.ui.comment

import android.os.Bundle
import android.view.View
import com.hin.movie.R
import com.hin.movie.databinding.FragmentCommentTabItemBinding
import com.hin.movie.ui.base.BaseFragment

class CommentTabFragment :
    BaseFragment<FragmentCommentTabItemBinding>(FragmentCommentTabItemBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtSeeAll.setOnClickListener { navigateTo(R.id.action_commentFragment) }
    }
}