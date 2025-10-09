package com.hin.flixcomix.ui.watch.data

import com.hin.flixcomix.data.entities.Episode
import com.hin.flixcomix.data.entities.Movie
import com.hin.flixcomix.data.entities.Server

data class MovieState(
    val movie: Movie? = null,
    val servers: List<Server>? = null,
    val currentEpisode: Episode? = null,
    val currentPositionEpisode: Int = 0,
)
