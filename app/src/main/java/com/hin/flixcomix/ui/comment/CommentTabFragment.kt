package com.hin.flixcomix.ui.comment

import android.os.Bundle
import android.view.View
import com.hin.flixcomix.R
import com.hin.flixcomix.databinding.FragmentCommentTabItemBinding
import com.hin.flixcomix.ui.base.BaseFragment

class CommentTabFragment :
    BaseFragment<FragmentCommentTabItemBinding>(FragmentCommentTabItemBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtSeeAll.setOnClickListener { navigateTo(R.id.action_commentFragment) }
    }
}