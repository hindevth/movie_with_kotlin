package com.hin.movie.ui.history

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.hin.movie.databinding.FragmentHistoryBinding
import com.hin.movie.ui.base.BaseFragment
import com.hin.movie.utils.extensions.gone
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>(FragmentHistoryBinding::inflate) {
    private var isSearching = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgSearch.setOnClickListener {
            toggleSearch()
        }
    }

    private fun toggleSearch() {
        if (!isSearching) {
            binding.imgLogo.gone()
            binding.txtTitle.gone()
            binding.imgSearch.gone()
            binding.searchEditText.apply {
                alpha = 0f
                visibility = View.VISIBLE
                animate().alpha(1f).setDuration(200).start()
                requestFocus()
            }

            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.searchEditText, InputMethodManager.SHOW_IMPLICIT)

        }
        isSearching = !isSearching
    }
}