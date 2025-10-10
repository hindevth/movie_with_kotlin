package com.hin.movie.ui.search.model

import com.hin.movie.data.entities.Country
import com.hin.movie.data.entities.Genre
import com.hin.movie.data.entities.Sort

data class FilterState(
    val search: String? = null,
    val sort: Sort? = null,
    val genre: Genre? = null,
    val country: Country? = null,
    val year: String? = null
) {
    override fun toString(): String {
        val params = mutableListOf<String>()

        search?.takeIf { it.isNotBlank() }?.let {
            params += "search=${it}"
        }
        sort?.let {
            params += "sort_type=${it.slug}" // hoặc it.name tùy bạn
        }

        genre?.let {
            params += "category=${it.slug}" // hoặc it.name tùy bạn
        }
        country?.let {
            params += "country=${it.slug}" // hoặc it.name
        }
        year?.let {
            params += "year=$it"
        }

        return params.joinToString("&", prefix = "?")
    }

    fun hasAnyFilter() = sort != null || country != null || genre != null
}
