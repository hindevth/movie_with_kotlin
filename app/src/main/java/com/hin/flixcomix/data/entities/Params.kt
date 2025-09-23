package com.hin.flixcomix.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Params(
    @SerializedName("type_slug")
    private val typeSlug: String? = null,
    @SerializedName("keyword")
    private val keyword: String? = null,
    @SerializedName("filterCategory")
    private val filterCategory: List<String>? = null,
    @SerializedName("filterCountry")
    private val filterCountry: List<String>? = null,
    @SerializedName("filterYear")
    private val filterYear: List<String>? = null,
    @SerializedName("filterType")
    private val filterType: List<String>? = null,
    @SerializedName("sortField")
    private val sortField: String? = null,
    @SerializedName("sortType")
    private val sortType: String? = null,
    @SerializedName("pagination")
    private val pagination: Pagination? = null,
): Parcelable