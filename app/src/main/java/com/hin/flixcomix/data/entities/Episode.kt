package com.hin.flixcomix.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Episode(
    @SerializedName("name") val name: String? = null,
    @SerializedName("slug") val slug: String? = null,
    @SerializedName("filename") val filename: String? = null,
    @SerializedName("link_embed") val linkEmbed: String? = null,
    @SerializedName("link_m3u8") val linkM3u8: String? = null,
): Parcelable
