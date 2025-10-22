package com.hin.movie.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.hin.movie.data.entities.Movie
import com.hin.movie.data.remote.firebase.BookmarkService
import javax.inject.Inject

class BookmarkRepository @Inject
constructor(
    private val bookmarkService: BookmarkService
) {
    suspend fun toggleBookmark(movie: Movie) = bookmarkService.toggleBookmark(movie)

    suspend fun addBookmark(movie: Movie) = bookmarkService.addBookmark(movie)

    suspend fun bookmarkIsExits(movie: Movie) = bookmarkService.bookmarkIsExist(movie)

    suspend fun removeBookmark(movie: Movie) = bookmarkService.removeBookmark(movie)

    suspend fun bookmarks(
        lastVisible: DocumentSnapshot? = null,
        limit: Long = 26
    ) = bookmarkService.bookmarks(lastVisible, limit)


}