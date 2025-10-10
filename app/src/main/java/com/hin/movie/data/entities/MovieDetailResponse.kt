package com.hin.movie.data.entities

import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(
    @SerializedName("msg") val msg: String? = null,
    @SerializedName("movie") val movie: Movie? = null,
    @SerializedName("episodes") val servers: List<Server>? = null,
)
