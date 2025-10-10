package com.hin.movie.ui.watch.data

import com.hin.movie.data.entities.Episode
import com.hin.movie.data.entities.Movie
import com.hin.movie.data.entities.Server

data class MovieState(
    val movie: Movie? = null,
    val servers: List<Server>? = null,
    val currentEpisode: Episode? = null,
    val currentPositionEpisode: Int = 0,
)
