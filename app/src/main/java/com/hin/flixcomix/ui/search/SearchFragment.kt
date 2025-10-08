package com.hin.flixcomix.ui.search

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.hin.flixcomix.data.entities.Country
import com.hin.flixcomix.data.entities.Genre
import com.hin.flixcomix.data.entities.Sort
import com.hin.flixcomix.databinding.FragmentSearchBinding
import com.hin.flixcomix.ui.base.BaseFragment
import com.hin.flixcomix.ui.base.SpaceItemDecoration
import com.hin.flixcomix.ui.search.adapter.AdapterFilterCountry
import com.hin.flixcomix.ui.search.adapter.AdapterFilterCountryListener
import com.hin.flixcomix.ui.search.adapter.AdapterFilterGenre
import com.hin.flixcomix.ui.search.adapter.AdapterFilterGenreListener
import com.hin.flixcomix.ui.search.adapter.AdapterFilterSearch
import com.hin.flixcomix.ui.search.adapter.AdapterFilterSort
import com.hin.flixcomix.ui.search.adapter.AdapterFilterSortListener
import com.hin.flixcomix.utils.Constants
import com.hin.flixcomix.utils.extensions.setupFlexbox
import timber.log.Timber

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

        val adapterSort = AdapterFilterSort()
        val adapterRegion = AdapterFilterCountry()
        val adapterGenre = AdapterFilterGenre()
        val adapterYear = AdapterFilterSearch()

        binding.includeFilter.rvSort.adapter = adapterSort
        binding.includeFilter.rvRegion.adapter = adapterRegion
        binding.includeFilter.rvGenre.adapter = adapterGenre
        binding.includeFilter.rvYear.adapter = adapterYear

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

        viewModel.filterData.observe(viewLifecycleOwner) { filterState ->
            adapterSort.setSelectedItem(filterState.sort)
            adapterGenre.setSelectedItem(filterState.genre)
            adapterRegion.setSelectedItem(filterState.country)

            binding.includeFilter.btnReset.isEnabled = filterState.hasAnyFilter()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.drawerLayout.translationX =
            Resources.getSystem().displayMetrics.widthPixels.toFloat()
    }

    private fun createFlexboxLayoutManager() = FlexboxLayoutManager(requireContext()).apply {
        justifyContent = JustifyContent.FLEX_START
        flexDirection = FlexDirection.ROW
    }

    fun onClick() {
        binding.btnBack.setOnClickListener { popNavigate() }
        binding.btnFilter.setOnClickListener { showDrawer() }
        binding.includeFilter.btnBack.setOnClickListener { popNavigate() }
        binding.includeFilter.btnReset.setOnClickListener { viewModel.resetFilter() }
        binding.includeFilter.btnApply.setOnClickListener { viewModel.fetchSearchAndFilter() }

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

}