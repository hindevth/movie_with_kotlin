package com.hin.movie.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Params(
    @SerializedName("type_slug")
    val typeSlug: String? = null,
    @SerializedName("keyword")
    val keyword: String? = null,
    @SerializedName("filterCategory")
    val filterCategory: List<String>? = null,
    @SerializedName("filterCountry")
    val filterCountry: List<String>? = null,
    @SerializedName("filterYear")
    val filterYear: List<String>? = null,
    @SerializedName("filterType")
    val filterType: List<String>? = null,
    @SerializedName("sortField")
    val sortField: String? = null,
    @SerializedName("sortType")
    val sortType: String? = null,
    @SerializedName("pagination")
    val pagination: Pagination? = null,
) : Parcelable