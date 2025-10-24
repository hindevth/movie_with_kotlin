package com.hin.movie.data.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.gson.Gson
import com.hin.movie.data.entities.Episode
import com.hin.movie.data.entities.EpisodeHistory
import com.hin.movie.data.entities.Movie
import com.hin.movie.data.entities.MovieHistory
import com.hin.movie.data.entities.MovieWithEpisodeJoin
import com.hin.movie.data.local.UserDataSource
import com.hin.movie.data.local.dao.MovieHistoryDao
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userDataSource: UserDataSource,
    private val movieHistoryDao: MovieHistoryDao
) {

    companion object {
        const val COLLECTION_MOVIE_HISTORY = "histories"
        const val COLLECTION_EPISODE_HISTORY = "episodes"
    }

    suspend fun addHistory(
        movie: Movie?,
        episode: Episode?,
        currentPositionEpisode: Long = 0,
        serverName: String = ""
    ) {
        val movieHistory = MovieHistory(
            slug = movie?.slug ?: "",
            movieJson = Gson().toJson(movie),
            createdAt = System.currentTimeMillis(),
        )

        movieHistoryDao.insertMovie(movieHistory)

        val episodeHistory = EpisodeHistory(
            id = 0,
            slug = episode?.slug ?: "",
            movieSlug = movie?.slug ?: "",
            episodeJson = Gson().toJson(episode),
            currentServerName = serverName,
            currentPositionEpisode = currentPositionEpisode
        )

        movieHistoryDao.insertEpisode(episodeHistory)
    }

    suspend fun getHistoryLast(movieSlug: String): EpisodeHistory? {
        return movieHistoryDao.getEpisodeByMovieSlug(movieSlug)
    }

    suspend fun getHistories(): List<MovieWithEpisodeJoin>? {
        return movieHistoryDao.getMovieWithLastEpisode()
    }

    suspend fun updatePosition(
        movieSlug: String,
        slugEpisode: String,
        currentPositionEpisode: Long
    ) {
        movieHistoryDao.updateCurrentPosition(movieSlug, slugEpisode, currentPositionEpisode)
    }

    suspend fun pullSyncFromFirebase() {
        val moviesSnap = firestore
            .collection(AuthService.COLLECTION_USER)
            .document(userDataSource.getUser()?.uid ?: "")
            .collection(COLLECTION_MOVIE_HISTORY)
            .get().await()


        val movieHistory =
            moviesSnap.documents.mapNotNull { it.toObject(MovieHistory::class.java) }

        movieHistory.forEach { movie ->
            movieHistoryDao.insertMovie(movie)

            val episodesSnap = firestore
                .collection(AuthService.COLLECTION_USER)
                .document(userDataSource.getUser()?.uid ?: "")
                .collection(COLLECTION_MOVIE_HISTORY)
                .document(movie.slug)
                .collection(COLLECTION_EPISODE_HISTORY)
                .get()
                .await()

            val episodeHistory =
                episodesSnap.documents.mapNotNull { snap -> snap.toObject(EpisodeHistory::class.java) }

            episodeHistory.forEach {
                movieHistoryDao.insertEpisode(it)
            }
        }
    }

    suspend fun syncEpisodes() {
        val movieHistory = movieHistoryDao.getMovieWithEpisodeNotSync()
        var tempMovieSlug = ""
        movieHistory?.forEach { history ->

            val movieMap = hashMapOf(
                "slug" to history.movie.slug,
                "movieJson" to history.movie.movieJson,
                "createdAt" to history.movie.createdAt,
                "updatedAt" to history.movie.updatedAt,
            )

            if (tempMovieSlug != history.movie.slug) {
                val movieSnap = firestore
                    .collection(AuthService.COLLECTION_USER)
                    .document(userDataSource.getUser()?.uid ?: "")
                    .collection(COLLECTION_MOVIE_HISTORY)
                    .document(history.movie.slug)
                    .set(movieMap, SetOptions.merge())
                    .await()
            }

            val episodeMap = hashMapOf(
                "id" to history.episode.id,
                "slug" to history.episode.slug,
                "movieSlug" to history.episode.movieSlug,
                "episodeJson" to history.episode.episodeJson,
                "currentPositionEpisode" to history.episode.currentPositionEpisode,
                "currentServerName" to history.episode.currentServerName,
                "isCompleted" to history.episode.isCompleted,
                "isSync" to 1,
                "createdAt" to history.episode.createdAt,
                "updatedAt" to history.episode.updatedAt,
            )
            val episodeSnap = firestore
                .collection(AuthService.COLLECTION_USER)
                .document(userDataSource.getUser()?.uid ?: "")
                .collection(COLLECTION_MOVIE_HISTORY)
                .document(history.movie.slug)
                .collection(COLLECTION_EPISODE_HISTORY)
                .document(history.episode.slug)
                .set(episodeMap, SetOptions.merge())
                .await()

            tempMovieSlug = history.movie.slug
            movieHistoryDao.updateSync(history.episode.slug)
        }
    }
}