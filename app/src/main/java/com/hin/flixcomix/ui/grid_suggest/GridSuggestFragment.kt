package com.hin.flixcomix.ui.grid_suggest

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.viewModels
import com.hin.flixcomix.R
import com.hin.flixcomix.data.entities.Movie
import com.hin.flixcomix.databinding.FragmentGridSugeestBinding
import com.hin.flixcomix.ui.base.BaseFragment
import com.hin.flixcomix.ui.base.GridSpacingItemDecoration
import com.hin.flixcomix.ui.grid_suggest.adapter.AdapterGridSuggest
import com.hin.flixcomix.ui.home.adapter.OnItemListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GridSuggestFragment() : BaseFragment<FragmentGridSugeestBinding>(
    FragmentGridSugeestBinding::inflate
) {
    private val viewModel: GridSuggestViewModel by viewModels()
    private var slugGenre: String? = null
    private var slugMovie: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            slugGenre = it.getString(ARG_GENRE)
            slugMovie = it.getString(ARG_MOVIE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spacingItemGrid = resources.getDimensionPixelSize(R.dimen.item_grid_spacing)
        binding.recyclerView.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                spacingItemGrid,
                true,
                0
            )
        )

        val adapter = AdapterGridSuggest()
        binding.recyclerView.adapter = adapter
        viewModel.loadMovies(slugGenre ?: "hanh-dong", slugMovie)

        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            adapter.submitList(movies)
        }

        adapter.setOnItemListener(object : OnItemListener {
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

        })
    }

    companion object {
        private const val ARG_GENRE = "slug_genre"
        private const val ARG_MOVIE = "slug_movie"

        fun newInstance(slugGenre: String?, slugMovie: String?): GridSuggestFragment {
            return GridSuggestFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_GENRE, slugGenre)
                    putString(ARG_MOVIE, slugMovie)
                }
            }
        }
    }
}