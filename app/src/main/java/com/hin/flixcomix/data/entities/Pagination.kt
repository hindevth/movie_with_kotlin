package com.hin.flixcomix.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pagination(
    @SerializedName("totalItems")
    private val totalItems: Int? = null,
    @SerializedName("totalItemsPerPage")
    private val totalItemsPerPage: Int? = null,
    @SerializedName("currentPage")
    private val currentPage: Int? = null,
    @SerializedName("totalPages")
    private val totalPages: Int? = null,
): Parcelable
