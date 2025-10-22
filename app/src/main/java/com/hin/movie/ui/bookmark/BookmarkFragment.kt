package com.hin.movie.ui.bookmark

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hin.movie.R
import com.hin.movie.data.entities.Movie
import com.hin.movie.databinding.FragmentBookmarkBinding
import com.hin.movie.ui.base.BaseFragment
import com.hin.movie.ui.base.GridSpacingItemDecoration
import com.hin.movie.ui.bookmark.adapter.AdapterBookmark
import com.hin.movie.ui.bookmark.adapter.ItemBookmarkListener
import com.hin.movie.utils.extensions.collectLifecycleFlow
import com.hin.movie.utils.extensions.gone
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class BookmarkFragment : BaseFragment<FragmentBookmarkBinding>(FragmentBookmarkBinding::inflate) {
    private val viewModel: BookmarkViewModel by viewModels()
    private var isSearching = false
    private lateinit var adapter: AdapterBookmark

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AdapterBookmark()
        binding.layoutBookmark.recyclerViewMovie.adapter = adapter
        binding.layoutBookmark.recyclerViewMovie.addOnScrollListener(recyclerViewListener)
        binding.layoutBookmark.recyclerViewMovie.addItemDecoration(
            GridSpacingItemDecoration(
                spacing = resources.getDimensionPixelSize(R.dimen.item_grid_spacing)
            )
        )

        onListener()
        onState()
    }

    fun onState() {
        collectLifecycleFlow(viewModel.uiState) {
            binding.layoutBookmark.root.isVisible = it.movies != null
            binding.layoutBookmarkEmpty.root.isVisible = it.movies == null
            adapter.submitList(it.movies)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isRefreshing = it
        }
    }

    fun onListener() {
        binding.imgSearch.setOnClickListener {
            toggleSearch()
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.clearLastDocument()
            viewModel.loadBookmark()
        }

        adapter.setItemBookmarkListener(object : ItemBookmarkListener {
            override fun onClick(item: Movie) {
                navigateTo(R.id.action_detailFragment, Bundle().apply {
                    putParcelable("movie", item)
                })
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

    private val recyclerViewListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy <= 0) return

            val layoutManager = recyclerView.layoutManager as? GridLayoutManager ?: return
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            if (!viewModel.isLoading.value!! && viewModel.uiState.value.lastDocument != null && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                viewModel.loadBookmark()
            }
        }
    }

    override fun onDestroyView() {
        binding.layoutBookmark.root.removeOnScrollListener(recyclerViewListener)
        super.onDestroyView()
    }
}