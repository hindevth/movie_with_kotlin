package com.hin.flixcomix.ui.search

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.hin.flixcomix.R
import com.hin.flixcomix.data.entities.Country
import com.hin.flixcomix.data.entities.Genre
import com.hin.flixcomix.data.entities.Movie
import com.hin.flixcomix.data.entities.Sort
import com.hin.flixcomix.databinding.FragmentSearchBinding
import com.hin.flixcomix.ui.base.BaseFragment
import com.hin.flixcomix.ui.base.GridSpacingItemDecoration
import com.hin.flixcomix.ui.base.SpaceItemDecoration
import com.hin.flixcomix.ui.search.adapter.AdapterFilterCountry
import com.hin.flixcomix.ui.search.adapter.AdapterFilterCountryListener
import com.hin.flixcomix.ui.search.adapter.AdapterFilterGenre
import com.hin.flixcomix.ui.search.adapter.AdapterFilterGenreListener
import com.hin.flixcomix.ui.search.adapter.AdapterFilterSearch
import com.hin.flixcomix.ui.search.adapter.AdapterFilterSort
import com.hin.flixcomix.ui.search.adapter.AdapterFilterSortListener
import com.hin.flixcomix.ui.search.adapter.AdapterMovieSearch
import com.hin.flixcomix.ui.search.adapter.OnItemListener
import com.hin.flixcomix.utils.Constants
import com.hin.flixcomix.utils.extensions.gone
import com.hin.flixcomix.utils.extensions.setupFlexbox
import com.hin.flixcomix.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {
    private val viewModel: SearchViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()

        binding.drawerLayout.translationX =
            Resources.getSystem().displayMetrics.widthPixels.toFloat()

        binding.includeFilter.rvSort.setupFlexbox()
        binding.includeFilter.rvGenre.setupFlexbox()
        binding.includeFilter.rvRegion.setupFlexbox()
        binding.includeFilter.rvYear.setupFlexbox()

        val spacingItemGrid = resources.getDimensionPixelSize(R.dimen.item_grid_spacing)
        binding.recyclerViewMovie.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                spacingItemGrid,
                true,
                0
            )
        )

        val adapterSort = AdapterFilterSort()
        val adapterRegion = AdapterFilterCountry()
        val adapterGenre = AdapterFilterGenre()
        val adapterYear = AdapterFilterSearch()
        val adapterMovie = AdapterMovieSearch()

        binding.includeFilter.rvSort.adapter = adapterSort
        binding.includeFilter.rvRegion.adapter = adapterRegion
        binding.includeFilter.rvGenre.adapter = adapterGenre
        binding.includeFilter.rvYear.adapter = adapterYear
        binding.recyclerViewMovie.adapter = adapterMovie
        binding.recyclerViewMovie.addOnScrollListener(recyclerViewListener)

        adapterSort.submitList(Constants.sort)
        adapterRegion.submitList(Constants.region)
        adapterGenre.submitList(Constants.genre)
        adapterYear.submitList(Constants.year)

        adapterSort.setOnListener(object : AdapterFilterSortListener {
            override fun onClick(item: Sort) {
                viewModel.changeSort(item)
            }
        })

        adapterRegion.setOnListener(object : AdapterFilterCountryListener {
            override fun onClick(item: Country) {
                viewModel.changeCountry(item)
            }
        })

        adapterGenre.setOnListener(object : AdapterFilterGenreListener {
            override fun onClick(item: Genre) {
                viewModel.changeGenre(item)
            }
        })


        binding.editTxtSearch.doAfterTextChanged {
            viewModel.setSearch(it.toString().trim())
        }

        adapterMovie.setOnClickItem(listenerMovie)

        viewModel.filterData.observe(viewLifecycleOwner) { filterState ->
            adapterSort.setSelectedItem(filterState.sort)
            adapterGenre.setSelectedItem(filterState.genre)
            adapterRegion.setSelectedItem(filterState.country)

            binding.includeFilter.btnReset.isEnabled = filterState.hasAnyFilter()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {bool ->
            if (bool) binding.spinLoading.show() else binding.spinLoading.hide()
        }

        viewModel.movies.observe(viewLifecycleOwner) {movies ->
            if (!movies.isNullOrEmpty()) {
                binding.includedNotFound.root.gone()
                binding.recyclerViewMovie.visible()
            }else {
                binding.includedNotFound.root.visible()
                binding.recyclerViewMovie.gone()
            }
            adapterMovie.submitList(movies)
            if (viewModel.pagination.value?.currentPage == 1){
                binding.recyclerViewMovie.scrollToPosition(0)
            }
        }
    }

    private val listenerMovie = object : OnItemListener {
        override fun onClick(
            imageView: ImageView,
            item: Movie,
            transitionName: String
        ) {
            navigateTo(R.id.action_detailFragment, Bundle().apply {
                putParcelable("movie", item)
                putString("transName", transitionName)
            })
        }
    }

    private val recyclerViewListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy <= 0) return

            val layoutManager = recyclerView.layoutManager as? GridLayoutManager ?: return
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            if (!viewModel.isLoading.value!! && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                viewModel.nextPage()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.drawerLayout.translationX =
            Resources.getSystem().displayMetrics.widthPixels.toFloat()
    }


    fun onClick() {
        binding.btnBack.setOnClickListener { popNavigate() }
        binding.btnFilter.setOnClickListener { showDrawer() }
        binding.includeFilter.btnBack.setOnClickListener { popNavigate() }
        binding.includeFilter.btnReset.setOnClickListener { viewModel.resetFilter() }
        binding.includeFilter.btnApply.setOnClickListener {
            hideDrawer()
            viewModel.changeCurrentPage(1)
            viewModel.fetchSearchAndFilter()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (binding.drawerLayout.translationX == 0f) {
                hideDrawer()
            } else {
                isEnabled = false
                popNavigate()
            }
        }
    }

    fun showDrawer() {
        binding.drawerLayout.visibility = View.VISIBLE

        binding.drawerLayout.animate()
            .translationX(0f)
            .setDuration(300)
            .start()
    }

    fun hideDrawer() {
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels.toFloat()

        binding.drawerLayout.animate()
            .translationX(screenWidth)
            .setDuration(300)
            .withEndAction { binding.drawerLayout.visibility = View.GONE }
            .start()
    }

    override fun onDestroyView() {
        binding.recyclerViewMovie.removeOnScrollListener(recyclerViewListener)
        super.onDestroyView()
    }

}