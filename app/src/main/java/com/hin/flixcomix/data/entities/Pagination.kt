package com.hin.flixcomix.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pagination(
    @SerializedName("totalItems")
    val totalItems: Int? = null,
    @SerializedName("totalItemsPerPage")
    val totalItemsPerPage: Int? = null,
    @SerializedName("currentPage")
    val currentPage: Int? = null,
    @SerializedName("totalPages")
    val totalPages: Int? = null,
) : Parcelable
