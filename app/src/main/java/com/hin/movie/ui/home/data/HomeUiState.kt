package com.hin.movie.ui.home.data

import com.hin.movie.data.entities.Movie

data class HomeUiState(
    val newMovies: List<Movie>? = emptyList(),
    val singleMovies: List<Movie>? = emptyList(),
    val seriesMovies: List<Movie>? = emptyList(),
    val tvShows: List<Movie>? = emptyList(),
    val cartoons: List<Movie>? = emptyList(),
)
