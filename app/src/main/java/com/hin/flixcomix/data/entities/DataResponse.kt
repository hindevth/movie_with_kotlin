package com.hin.flixcomix.data.entities

import com.google.gson.annotations.SerializedName

data class DataResponse(
    @SerializedName("titlePage") val titlePage: String? = null,
    @SerializedName("items") val items: List<Movie>? = null,
    @SerializedName("params") val params: Params? = null,
)
