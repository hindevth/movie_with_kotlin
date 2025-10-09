package com.hin.flixcomix.ui.movie_more

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hin.flixcomix.R
import com.hin.flixcomix.data.entities.Movie
import com.hin.flixcomix.databinding.FragmentMovieMoreBinding
import com.hin.flixcomix.databinding.FragmentMovieMoreBinding.inflate
import com.hin.flixcomix.ui.base.BaseFragment
import com.hin.flixcomix.ui.base.GridSpacingItemDecoration
import com.hin.flixcomix.ui.movie_more.adapter.AdapterMovieMore
import com.hin.flixcomix.ui.movie_more.adapter.OnItemListener
import com.hin.flixcomix.utils.MovieType
import com.hin.flixcomix.utils.extensions.getParcelableListCompat
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_SLUG_MOVIE = "slug"
private const val ARG_MOVIES = "movies"

@AndroidEntryPoint
class MovieMoreFragment :
    BaseFragment<FragmentMovieMoreBinding>(FragmentMovieMoreBinding::inflate) {
    private val viewModel: MovieMoreViewModel by viewModels()
    private var slugMovie: String? = null
    private var movies: List<Movie>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            slugMovie = it.getString(ARG_SLUG_MOVIE)
            movies = it.getParcelableListCompat<Movie>(ARG_MOVIES)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
        onClick()
        viewModel.setTypeMovie(slugMovie)
        viewModel.setMovies(movies)

        val adapterMovie = AdapterMovieMore()
        binding.recyclerView.adapter = adapterMovie
        binding.recyclerView.addOnScrollListener(recyclerViewListener)

        val spacingItemGrid = resources.getDimensionPixelSize(R.dimen.item_grid_spacing)
        binding.recyclerView.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                spacingItemGrid,
                true,
                0
            )
        )

        adapterMovie.setOnClickItem(movieListener)

        viewModel.typeMovie.observe(viewLifecycleOwner) { slugMovie ->
            viewModel.fetchMovie()
        }

        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            if (movies != null) adapterMovie.submitList(movies)
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

    private val movieListener = object : OnItemListener {
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

    fun setTitle() {
        binding.txtTitle.text = when (slugMovie) {
            MovieType.PHIM_BO -> getString(R.string.series_movie)
            MovieType.PHIM_LE -> getString(R.string.single_movie)
            MovieType.TV_SHOWS -> getString(R.string.tv_shows)
            MovieType.HOAT_HINH -> getString(R.string.cartoons)
            else -> getString(R.string.new_episode_releases)
        }
    }

    fun onClick(){
        binding.btnBack.setOnClickListener { popNavigate() }
    }

    override fun onDestroyView() {
        binding.recyclerView.removeOnScrollListener(recyclerViewListener)
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance(slug: String) =
            MovieMoreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SLUG_MOVIE, slug)
                }
            }
    }
}