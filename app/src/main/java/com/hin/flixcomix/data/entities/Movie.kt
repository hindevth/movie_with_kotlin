package com.hin.flixcomix.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.hin.flixcomix.utils.Constants
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("slug") val slug: String? = null,
    @SerializedName("origin_name") val originName: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("poster_url") val posterUrl: String? = null,
    @SerializedName("thumb_url") val thumbUrl: String? = null,
    @SerializedName("sub_docquyen") val subDocQuyen: Boolean? = null,
    @SerializedName("time") val time: String? = null,
    @SerializedName("episode_current") val episodeCurrent: String? = null,
    @SerializedName("quality") val quality: String? = null,
    @SerializedName("lang") val lang: String? = null,
    @SerializedName("content") val content: String? = null,
    @SerializedName("actor") val actor: List<String>? = null,
    @SerializedName("director") val director: List<String>? = null,
    @SerializedName("year") val year: Int? = null,
    @SerializedName("category") val category: List<Genre>? = null,
    @SerializedName("country") val country: List<Country>? = null,
    @SerializedName("tmdb") val tmdb: Tmdb? = null,
    @SerializedName("created") val created: Created? = null,
    @SerializedName("modified") val modified: Modified? = null,
) : Parcelable {
    val posterImageUrl: String?
        get() {
            if (posterUrl != null) {
                return if (posterUrl.startsWith("http")) posterUrl else "${Constants.APP_DOMAIN_CDN_IMAGE}/$posterUrl"
            }
            return posterUrl
        }

    val thumbImageUrl: String?
        get() {
            if (thumbUrl != null) {
                return if (thumbUrl.startsWith("http")) thumbUrl else "${Constants.APP_DOMAIN_CDN_IMAGE}/$thumbUrl"
            }
            return thumbUrl
        }

    val categories: String?
        get() {
            if (!category.isNullOrEmpty()) {
                return category.joinToString { it.name.toString() }
            }
            return ""
        }
}