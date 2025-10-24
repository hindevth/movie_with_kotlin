package com.hin.movie.data.repository

import com.hin.movie.data.entities.Episode
import com.hin.movie.data.entities.EpisodeHistory
import com.hin.movie.data.entities.Movie
import com.hin.movie.data.entities.MovieWithEpisodeJoin
import com.hin.movie.data.remote.firebase.HistoryService
import javax.inject.Inject

class HistoryRepository @Inject constructor(private val historyService: HistoryService) {
    suspend fun addHistory(
        movie: Movie?,
        episode: Episode?,
        currentPositionEpisode: Long = 0,
        serverName: String = ""
    ) {
        historyService.addHistory(movie, episode, currentPositionEpisode, serverName)
    }

    suspend fun getHistories(): List<MovieWithEpisodeJoin>? {
        return historyService.getHistories()
    }

    suspend fun getHistoryLast(movieSlug: String): EpisodeHistory? {
        return historyService.getHistoryLast(movieSlug)
    }

    suspend fun updatePosition(
        movieSlug: String,
        slugEpisode: String,
        currentPositionEpisode: Long
    ) {
        historyService.updatePosition(movieSlug, slugEpisode, currentPositionEpisode)
    }


    suspend fun pullFromFirebase() {
        historyService.pullSyncFromFirebase()
    }

    suspend fun syncHistories() {
        historyService.syncEpisodes()
    }
}