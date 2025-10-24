package com.hin.movie.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.Gson


@Entity(
    tableName = "episode_history", foreignKeys = [
        ForeignKey(
            entity = MovieHistory::class,
            parentColumns = ["slug"],
            childColumns = ["movieSlug"],
            onDelete = ForeignKey.CASCADE
        )
    ], indices = [Index("movieSlug")]
)
data class EpisodeHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val slug: String,
    val movieSlug: String,
    val episodeJson: String,
    val currentServerName: String,
    val currentPositionEpisode: Long,
    val updatedAt: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val isSync: Boolean = false,
    val isCompleted: Boolean = false
) {
    val episode: Episode get() = Gson().fromJson(episodeJson, Episode::class.java)

}