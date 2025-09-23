package com.hin.flixcomix.data.repository

import com.hin.flixcomix.data.entities.MovieDataResponse
import com.hin.flixcomix.data.entities.MovieDetailResponse
import com.hin.flixcomix.data.entities.MovieItemsResponse
import com.hin.flixcomix.data.remote.apis.MovieService
import javax.inject.Inject

class MovieRepository @Inject constructor(val movieService: MovieService) {
    suspend fun getNewMovie(page: Int? = 1, limit: Int? = 10): MovieItemsResponse {
        return movieService.getNewMovie(page, limit)
    }

    suspend fun getMovieByType(
        type: String,
        page: Int? = 1,
        limit: Int? = 10,
        sortType: String? = null,
        sortLang: String? = null,
        category: String? = null,
        country: String? = null,
        year: String? = null,
    ): MovieDataResponse {
        return movieService.getMovieByType(
            type,
            page,
            limit,
            sortType,
            sortLang,
            category,
            country,
            year
        )
    }

    suspend fun getMovieByGenre(
        type: String,
        page: Int? = 1,
        limit: Int? = 10,
        sortType: String? = null,
        sortLang: String? = null,
        country: String? = null,
        year: String? = null,
    ): MovieDataResponse {
        return movieService.getMovieByGenre(
            type,
            page,
            limit,
            sortType,
            sortLang,
            country,
            year
        )
    }

    suspend fun getMovieSearch(
        keyword: String,
        page: Int? = 1,
        limit: Int? = 25,
        sortType: String? = null,
        sortLang: String? = null,
        category: String? = null,
        country: String? = null,
        year: String? = null,
    ): MovieDataResponse {
        return movieService.getMovieSearch(
            keyword,
            page,
            limit,
            sortType,
            sortLang,
            category,
            country,
            year
        )
    }

    suspend fun getMovieDetail(slug: String): MovieDetailResponse {
        return movieService.getMovieDetail(slug)
    }
}