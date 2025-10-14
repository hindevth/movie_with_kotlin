package com.hin.movie.data.entities

import com.google.gson.annotations.SerializedName

data class Setting(
    @SerializedName("language")
    val language: String = "auto",

    @SerializedName("theme")
    val theme: String = "auto"
)