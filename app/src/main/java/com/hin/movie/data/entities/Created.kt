package com.hin.movie.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Created(
    @SerializedName("time") val time: String? = null
) : Parcelable

