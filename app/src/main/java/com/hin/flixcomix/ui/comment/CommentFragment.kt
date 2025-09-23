package com.hin.flixcomix.ui.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.hin.flixcomix.R
import com.hin.flixcomix.databinding.FragmentCommentBinding
import com.hin.flixcomix.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentFragment : BaseFragment<FragmentCommentBinding>(FragmentCommentBinding::inflate) {
    private val viewModel: CommentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
    }

    fun onClick(){
        binding.imgBack.setOnClickListener { popNavigate() }
    }
}