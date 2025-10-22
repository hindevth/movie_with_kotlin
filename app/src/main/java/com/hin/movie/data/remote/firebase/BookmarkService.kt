package com.hin.movie.data.remote.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.hin.movie.data.entities.Movie
import com.hin.movie.data.local.UserDataSource
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userDataSource: UserDataSource
) {

    companion object {
        const val COLLECTION_BOOKMARK = "bookmarks"
    }

    suspend fun toggleBookmark(movie: Movie) {
        val user = userDataSource.getUser()
        if (user == null) {
            throw Exception("User not found")
        }

        val movieExist = bookmarkIsExist(movie)
        if (movieExist) {
            removeBookmark(movie)
        } else {
            addBookmark(movie)
        }
    }


    suspend fun addBookmark(movie: Movie) {
        val user = userDataSource.getUser()
        if (user == null) {
            throw Exception("User not found")
        }

        val searchKey = buildList {
            add(movie.name)
            add(movie.originName)
            add(movie.slug)
            addAll(movie.actor!!)
        }

        val map = mapOf(
            "movie" to movie,
            "searchKey" to searchKey,
            "deleted" to false,
            "createdAt" to System.currentTimeMillis(),
            "updatedAt" to System.currentTimeMillis()
        )

        firestore.collection(AuthService.COLLECTION_USER)
            .document(user.uid)
            .collection(COLLECTION_BOOKMARK)
            .document(movie.slug.toString()).set(map, SetOptions.merge())
            .await()
    }

    suspend fun bookmarkIsExist(movie: Movie): Boolean {
        val user = userDataSource.getUser()
        if (user == null) {
            throw Exception("User not found")
        }

        val documentSnapshot = firestore.collection(AuthService.COLLECTION_USER)
            .document(user.uid)
            .collection(COLLECTION_BOOKMARK)
            .document(movie.slug.toString())
            .get()
            .await()
        return documentSnapshot.exists()
    }

    suspend fun removeBookmark(movie: Movie) {
        val user = userDataSource.getUser()
        if (user == null) {
            throw Exception("User not found")
        }

        val map = mapOf(
            "deleted" to true,
            "updatedAt" to System.currentTimeMillis()
        )

        firestore.collection(AuthService.COLLECTION_USER)
            .document(user.uid)
            .collection(COLLECTION_BOOKMARK)
            .document(movie.slug.toString())
            .update(map)
            .await()
    }

    suspend fun bookmarks(
        lastVisible: DocumentSnapshot? = null,
        limit: Long = 26
    ): Pair<List<Movie?>, DocumentSnapshot?> {
        val user = userDataSource.getUser()
        if (user == null) {
            throw Exception("User not found")
        }

        try {
            val querySnapshot = firestore.collection(AuthService.COLLECTION_USER)
                .document(user.uid)
                .collection(COLLECTION_BOOKMARK)
                .whereNotEqualTo("deleted", true)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(limit)


            if (lastVisible != null) {
                querySnapshot.startAfter(lastVisible)
            }

            val bookmarkSnapshot = querySnapshot.get().await()
            val movies = bookmarkSnapshot.documents.map {
                it.get("movie", Movie::class.java)
            }

            return Pair(movies, bookmarkSnapshot.documents.lastOrNull())
        } catch (e: Exception) {
            Timber.e(e)
            return Pair(emptyList(), null)
        }
    }

}