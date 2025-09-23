package com.hin.flixcomix.data.entities

import com.google.gson.annotations.SerializedName

data class MovieItemsResponse(
    @SerializedName("msg") val msg: String? = null,
    @SerializedName("items") val items: List<Movie>? = null,
    @SerializedName("pagination") val pagination: Pagination? = null,
)
