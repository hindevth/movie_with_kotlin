package com.hin.movie.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hin.movie.data.entities.EpisodeHistory
import com.hin.movie.data.entities.MovieHistory
import com.hin.movie.data.entities.MovieWithEpisodeJoin

@Dao
interface MovieHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movieHistory: MovieHistory)

    @Query("SELECT * FROM movie_history WHERE slug = :slug LIMIT 1")
    suspend fun getMovieBySlug(slug: String): MovieHistory?

    @Query("SELECT * FROM movie_history")
    suspend fun getMovies(): List<MovieHistory>?

    @Query(
        """
        SELECT 
            m.slug AS movie_slug,
            m.movieJson AS movie_movieJson,
            m.createdAt AS movie_createdAt,
            m.updatedAt AS movie_updatedAt,
            
            e.id AS ep_id,
            e.slug AS ep_slug,
            e.movieSlug AS ep_movieSlug,
            e.episodeJson AS ep_episodeJson,
            e.currentPositionEpisode AS ep_currentPositionEpisode,
            e.currentServerName AS ep_currentServerName,
            e.isCompleted AS ep_isCompleted,
            e.isSync AS ep_isSync,
            e.createdAt AS ep_createdAt,
            e.updatedAt AS ep_updatedAt
        FROM movie_history m
        LEFT JOIN episode_history e 
            ON e.movieSlug = m.slug
        INNER JOIN (
            SELECT movieSlug, MAX(updatedAt) AS lastUpdate
            FROM episode_history
            GROUP BY movieSlug
        ) lastEpisode
            ON lastEpisode.movieSlug = e.movieSlug
            AND lastEpisode.lastUpdate = e.updatedAt
    """
    )
    suspend fun getMovieWithLastEpisode(): List<MovieWithEpisodeJoin>?

    @Query("DELETE FROM MOVIE_HISTORY")
    suspend fun deleteMovieAll()

    @Query(
        """
        SELECT 
            m.slug AS movie_slug,
            m.movieJson AS movie_movieJson,
            m.createdAt AS movie_createdAt,
            m.updatedAt AS movie_updatedAt,
            
            e.id AS ep_id,
            e.slug AS ep_slug,
            e.movieSlug AS ep_movieSlug,
            e.episodeJson AS ep_episodeJson,
            e.currentPositionEpisode AS ep_currentPositionEpisode,
            e.currentServerName AS ep_currentServerName,
            e.isCompleted AS ep_isCompleted,
            e.isSync AS ep_isSync,
            e.createdAt AS ep_createdAt,
            e.updatedAt AS ep_updatedAt
        FROM movie_history m
        LEFT JOIN episode_history e 
        ON m.slug = e.movieSlug
        WHERE e.isSync = 0
    """
    )
    suspend fun getMovieWithEpisodeNotSync(): List<MovieWithEpisodeJoin>?

    // Episode
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisode(episodeHistory: EpisodeHistory)

    @Query("SELECT * FROM episode_history WHERE slug = :slug LIMIT 1")
    suspend fun getEpisodeBySlug(slug: String): EpisodeHistory?

    @Query("SELECT * FROM episode_history WHERE movieSlug = :movieSlug ORDER BY updatedAt DESC LIMIT 1")
    suspend fun getEpisodeByMovieSlug(movieSlug: String): EpisodeHistory?

    @Query("SELECT * FROM episode_history WHERE movieSlug = :movieSlug ORDER BY updatedAt DESC")
    suspend fun getEpisodesByMovieSlug(movieSlug: String): List<EpisodeHistory>?

    @Query("SELECT * FROM episode_history WHERE isSync = 0")
    suspend fun getNotSync(): List<EpisodeHistory>?

    @Query("UPDATE episode_history SET isSync = 1 WHERE slug = :slug")
    suspend fun updateSync(slug: String)

    @Query("UPDATE episode_history SET isCompleted = 1, isSync = 0 WHERE slug = :slug")
    suspend fun updateCompleted(slug: String)

    @Query("UPDATE episode_history SET currentPositionEpisode = :currentPositionEpisode, isSync = 0 WHERE slug = :slug AND movieSlug = :movieSlug")
    suspend fun updateCurrentPosition(movieSlug: String, slug: String, currentPositionEpisode: Long)

    @Query("DELETE FROM episode_history")
    suspend fun deleteEpisodeAll()
}