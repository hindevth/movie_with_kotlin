package com.hin.movie.ui.home

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.FullScreenCarouselStrategy
import com.hin.movie.R
import com.hin.movie.data.entities.Movie
import com.hin.movie.databinding.FragmentHomeBinding
import com.hin.movie.ui.base.BaseFragment
import com.hin.movie.ui.base.SpaceItemDecoration
import com.hin.movie.ui.home.adapter.AdapterMovie
import com.hin.movie.ui.home.adapter.AdapterSlide
import com.hin.movie.ui.home.adapter.OnItemListener
import com.hin.movie.ui.home.adapter.OnPlayItemListener
import com.hin.movie.utils.MovieType
import com.hin.movie.utils.extensions.collectLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val viewModel: HomeViewModel by activityViewModels()
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

            val spacingItemGrid = resources.getDimensionPixelSize(R.dimen.item_grid_spacing)
            rvNewMovie.addItemDecoration(SpaceItemDecoration(spacingItemGrid))
            rvSingleMovie.addItemDecoration(SpaceItemDecoration(spacingItemGrid))
            rvSeriesMovie.addItemDecoration(SpaceItemDecoration(spacingItemGrid))
            rvCartoons.addItemDecoration(SpaceItemDecoration(spacingItemGrid))
            rvTVShows.addItemDecoration(SpaceItemDecoration(spacingItemGrid))
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

        binding.apply {
            txtMoreNew.setOnClickListener {
                navigateTo(R.id.action_movieMoreFragment, Bundle().apply {
                    putString("slug", "new")
                    putParcelableArrayList("movies", ArrayList(viewModel.uiState.value.newMovies!!))
                })
            }
            txtMoreSing.setOnClickListener {
                navigateTo(R.id.action_movieMoreFragment, Bundle().apply {
                    putString("slug", MovieType.PHIM_LE)
                    putParcelableArrayList("movies", ArrayList(viewModel.uiState.value.singleMovies!!))
                })
            }
            txtMoreSeries.setOnClickListener {
                navigateTo(R.id.action_movieMoreFragment, Bundle().apply {
                    putString("slug", MovieType.PHIM_BO)
                    putParcelableArrayList("movies", ArrayList(viewModel.uiState.value.seriesMovies!!))
                })
            }
            txtMoreTV.setOnClickListener {
                navigateTo(R.id.action_movieMoreFragment, Bundle().apply {
                    putString("slug", MovieType.TV_SHOWS)
                    putParcelableArrayList("movies", ArrayList(viewModel.uiState.value.tvShows!!))
                })
            }
            txtMoreCartoons.setOnClickListener {
                navigateTo(R.id.action_movieMoreFragment, Bundle().apply {
                    putString("slug", MovieType.HOAT_HINH)
                    putParcelableArrayList("movies", ArrayList(viewModel.uiState.value.cartoons!!))
                })
            }
        }
    }

}