package com.hin.movie.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

@Entity(tableName = "movie_history")
data class MovieHistory(
    @PrimaryKey
    val slug: String,
    val movieJson: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
) {
    val movie: Movie
        get() = Gson().fromJson(movieJson, Movie::class.java)
}

data class MovieWithEpisodeJoin(
    @Embedded("movie_") val movie: MovieHistory,
    @Embedded("ep_") val episode: EpisodeHistory
)
