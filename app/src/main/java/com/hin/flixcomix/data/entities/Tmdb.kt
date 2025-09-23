package com.hin.flixcomix.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tmdb(
    @SerializedName("type")
    private val type: String? = null,
    @SerializedName("id")
    private val id: String? = null,
    @SerializedName("season")
    private val season: String? = null,
    @SerializedName("vote_average")
    private val voteAverage: Float? = null,
    @SerializedName("vote_count")
    private val voteCount: Int? = null,
): Parcelable
