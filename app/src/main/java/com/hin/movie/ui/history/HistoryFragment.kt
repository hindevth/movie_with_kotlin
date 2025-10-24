package com.hin.movie.ui.history

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.hin.movie.R
import com.hin.movie.data.entities.Movie
import com.hin.movie.data.entities.MovieWithEpisodeJoin
import com.hin.movie.databinding.FragmentHistoryBinding
import com.hin.movie.ui.base.BaseFragment
import com.hin.movie.ui.base.GridSpacingItemDecoration
import com.hin.movie.ui.bookmark.adapter.ItemBookmarkListener
import com.hin.movie.ui.history.adapter.AdapterHistory
import com.hin.movie.ui.history.adapter.ItemHistoryListener
import com.hin.movie.utils.extensions.collectLifecycleFlow
import com.hin.movie.utils.extensions.gone
import com.hin.movie.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>(FragmentHistoryBinding::inflate) {
    private val viewModel: HistoryViewModel by viewModels()
    private var isSearching = false
    private lateinit var adapter: AdapterHistory
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AdapterHistory()
        binding.recyclerViewMovie.adapter = adapter
        binding.recyclerViewMovie.addItemDecoration(
            GridSpacingItemDecoration(
                spacing = resources.getDimensionPixelSize(R.dimen.item_grid_spacing)
            )
        )

        onListener()
        onClick()

    }

    fun onListener() {
        collectLifecycleFlow(viewModel.uiState) { state ->
            if (state.movies != null){
                binding.layoutHistoryEmpty.root.gone()
                binding.recyclerViewMovie.visible()
                adapter.setSublistMovie(state.movies)
            }else{
                binding.layoutHistoryEmpty.root.visible()
                binding.recyclerViewMovie.gone()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isRefreshing = it
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadHistories()
        }

        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            adapter.filter(text.toString())
        }
    }

    fun onClick() {
        binding.imgSearch.setOnClickListener {
            toggleSearch()
        }

        adapter.setItemBookmarkListener(object : ItemHistoryListener {

            override fun onClick(item: MovieWithEpisodeJoin) {
                navigateTo(R.id.action_detailFragment, Bundle().apply {
                    putParcelable("movie", item.movie.movie)
                })
            }

            override fun onClickDelete(item: MovieWithEpisodeJoin) {
            }
        })
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