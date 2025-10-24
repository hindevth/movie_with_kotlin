package com.hin.movie.ui.history.data

import com.hin.movie.data.entities.MovieWithEpisodeJoin

data class UIState(
    val movies: List<MovieWithEpisodeJoin>?= null,
//    val lastDocument: DocumentSnapshot? = null
)