package com.hin.flixcomix.ui.home

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.viewModels
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.FullScreenCarouselStrategy
import com.hin.flixcomix.R
import com.hin.flixcomix.data.entities.Movie
import com.hin.flixcomix.databinding.FragmentHomeBinding
import com.hin.flixcomix.ui.base.BaseFragment
import com.hin.flixcomix.ui.home.adapter.AdapterMovie
import com.hin.flixcomix.ui.home.adapter.AdapterSlide
import com.hin.flixcomix.ui.home.adapter.OnItemListener
import com.hin.flixcomix.ui.home.adapter.OnPlayItemListener
import com.hin.flixcomix.utils.extensions.collectLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val viewModel: HomeViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()

        viewModel.loadNewMovies()

        binding.recyclerView.layoutManager = CarouselLayoutManager(FullScreenCarouselStrategy())
        CarouselSnapHelper().attachToRecyclerView(binding.recyclerView)

        val adapter = AdapterSlide()
        val adapterNew = AdapterMovie()
        val adapterSing = AdapterMovie()
        val adapterSeries = AdapterMovie()
        val adapterCartoons = AdapterMovie()
        val adapterTV = AdapterMovie()

        binding.apply {
            recyclerView.adapter = adapter
            rvNewMovie.adapter = adapterNew
            rvSingleMovie.adapter = adapterSing
            rvSeriesMovie.adapter = adapterSeries
            rvCartoons.adapter = adapterCartoons
            rvTVShows.adapter = adapterTV
        }

        collectLifecycleFlow(viewModel.uiState) { state ->
            adapter.submitList(state.newMovies)
            adapterNew.submitList(state.newMovies)
            adapterSing.submitList(state.singleMovies)
            adapterSeries.submitList(state.seriesMovies)
            adapterTV.submitList(state.tvShows)
            adapterCartoons.submitList(state.cartoons)
        }

        adapter.setOnPlayClick(object : OnPlayItemListener {
            override fun onClick(item: Movie, transName: String) {
                navigateTo(R.id.action_detailFragment, Bundle().apply {
                    putParcelable("movie", item)
                    putString("transName", transName)
                })
            }
        })

        adapterNew.setOnClickItem(object : OnItemListener {
            override fun onClick(imageView: ImageView, item: Movie, transitionName: String) {
                navigateTo(R.id.action_detailFragment, Bundle().apply {
                    putParcelable("movie", item)
                    putString("transName", transitionName)
                })
            }
        })

        adapterSing.setOnClickItem(object : OnItemListener {
            override fun onClick(imageView: ImageView, item: Movie, transitionName: String) {
                navigateTo(R.id.action_detailFragment, Bundle().apply {
                    putParcelable("movie", item)
                    putString("transName", transitionName)
                })
            }
        })

        adapterSeries.setOnClickItem(object : OnItemListener {
            override fun onClick(imageView: ImageView, item: Movie, transitionName: String) {
                navigateTo(R.id.action_detailFragment, Bundle().apply {
                    putParcelable("movie", item)
                    putString("transName", transitionName)
                })
            }
        })

        adapterTV.setOnClickItem(object : OnItemListener {
            override fun onClick(imageView: ImageView, item: Movie, transitionName: String) {
                navigateTo(R.id.action_detailFragment, Bundle().apply {
                    putParcelable("movie", item)
                    putString("transName", transitionName)
                })
            }
        })

        adapterCartoons.setOnClickItem(object : OnItemListener {
            override fun onClick(imageView: ImageView, item: Movie, transitionName: String) {
                navigateTo(R.id.action_detailFragment, Bundle().apply {
                    putParcelable("movie", item)
                    putString("transName", transitionName)
                })
            }
        })

    }

    fun onClick() {
        binding.imgSearch.setOnClickListener {
            navigateTo(R.id.action_searchFragment)
        }
    }

}