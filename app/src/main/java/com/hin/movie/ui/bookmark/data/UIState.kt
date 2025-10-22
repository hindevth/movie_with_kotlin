package com.hin.movie.ui.bookmark.data

import com.google.firebase.firestore.DocumentSnapshot
import com.hin.movie.data.entities.Movie

data class UIState(
    val movies: List<Movie?>? = null,
    val searchKey: String? = null,
    val lastDocument: DocumentSnapshot? = null,
)