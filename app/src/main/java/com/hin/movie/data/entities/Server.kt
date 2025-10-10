package com.hin.movie.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Server(
    @SerializedName("server_name") val name: String? = null,
    @SerializedName("server_data") val episodes: List<Episode>? = null,
): Parcelable
