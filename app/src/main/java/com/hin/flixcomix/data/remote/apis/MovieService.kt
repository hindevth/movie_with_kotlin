package com.hin.flixcomix.data.remote.apis

import com.hin.flixcomix.data.entities.MovieDataResponse
import com.hin.flixcomix.data.entities.MovieDetailResponse
import com.hin.flixcomix.data.entities.MovieItemsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("danh-sach/phim-moi-cap-nhat-v3")
    suspend fun getNewMovie(
        @Query("page") page: Int? = 1,
        @Query("limit") limit: Int? = 10
    ): MovieItemsResponse

    @GET("v1/api/danh-sach/{movie_type}")
    suspend fun getMovieByType(
        @Path("movie_type") movieType: String,
        @Query("page") page: Int? = 1,
        @Query("limit") limit: Int? = 10,
        @Query("sort_type") sortType: String? = null,
        @Query("sort_lang") sortLang: String? = null,
        @Query("category") category: String? = null,
        @Query("country") country: String? = null,
        @Query("year") year: String? = null,
    ): MovieDataResponse

    @GET("v1/api/the-loai/{slug_genre}")
    suspend fun getMovieByGenre(
        @Path("slug_genre") slugGenre: String,
        @Query("page") page: Int? = 1,
        @Query("limit") limit: Int? = 10,
        @Query("sort_type") sortType: String? = null,
        @Query("sort_lang") sortLang: String? = null,
        @Query("country") country: String? = null,
        @Query("year") year: String? = null,
    ): MovieDataResponse

    @GET("v1/api/tim-kiem")
    suspend fun getMovieSearch(
        @Query("keyword") keyword: String,
        @Query("page") page: Int? = 1,
        @Query("limit") limit: Int? = 10,
        @Query("sort_type") sortType: String? = null,
        @Query("sort_lang") sortLang: String? = null,
        @Query("category") category: String? = null,
        @Query("country") country: String? = null,
        @Query("year") year: String? = null,
    ): MovieDataResponse

    @GET("phim/{slug}")
    suspend fun getMovieDetail(
        @Path("slug") slug: String
    ): MovieDetailResponse
}