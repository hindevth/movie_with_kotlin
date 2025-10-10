package com.hin.movie.ui.comment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.hin.movie.databinding.FragmentCommentBinding
import com.hin.movie.ui.base.BaseFragment
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