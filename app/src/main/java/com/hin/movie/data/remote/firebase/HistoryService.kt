package com.hin.movie.data.remote.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.hin.movie.data.entities.Episode
import com.hin.movie.data.entities.Movie
import com.hin.movie.data.local.UserDataSource
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userDataSource: UserDataSource
) {

    companion object {
        const val COLLECTION_HISTORY = "histories"
    }

    suspend fun addHistory(movie: Movie, serverName: String, episode: Episode) {
        val user = userDataSource.getUser()
        if (user == null) {
            throw Exception("User not found")
        }

        val map = mapOf(
            "movie" to movie,
            "searchKey" to listOf(movie.name, movie.originName, movie.slug, movie.actor),
            "serverName" to serverName,
            "currentEpisode" to episode,
            "currentTime" to 0,
            "deleted" to false,
            "createdAt" to System.currentTimeMillis(),
            "updatedAt" to System.currentTimeMillis()
        )

        firestore.collection(AuthService.COLLECTION_USER)
            .document(user.uid)
            .collection(COLLECTION_HISTORY)
            .document(movie.slug.toString()).set(map, SetOptions.merge())
            .await()
    }

    suspend fun updateCurrentTime(movie: Movie, currentTime: Long) {
        val user = userDataSource.getUser()
        if (user == null) {
            throw Exception("User not found")
        }

        val map = mapOf(
            "currentTime" to currentTime,
            "updatedAt" to System.currentTimeMillis()
        )

        firestore.collection(AuthService.COLLECTION_USER)
            .document(user.uid)
            .collection(COLLECTION_HISTORY)
            .document(movie.slug.toString()).update(map)
            .await()
    }

    suspend fun getHistory(movie: Movie): Movie? {
        val user = userDataSource.getUser()
        if (user == null) {
            throw Exception("User not found")
        }

        val documentSnapshot = firestore.collection(AuthService.COLLECTION_USER)
            .document(user.uid)
            .collection(COLLECTION_HISTORY)
            .document(movie.slug.toString())
            .get()
            .await()

        return documentSnapshot.toObject(Movie::class.java)
    }

    suspend fun removeHistory(movie: Movie) {
        val user = userDataSource.getUser()
        if (user == null) {
            throw Exception("User not found")
        }

        val map = mapOf(
            "deleted" to true,
            "updatedAt" to System.currentTimeMillis()
        )

        firestore.collection("users")
            .document(user.uid)
            .collection(COLLECTION_HISTORY)
            .document(movie.slug.toString())
            .update(map)
            .await()
    }

    suspend fun getHistories(
        searchKey: String? = null,
        lastVisible: DocumentSnapshot? = null,
        limit: Long = 26
    ): Pair<List<Movie?>, DocumentSnapshot?> {
        val user = userDataSource.getUser()
        if (user == null) {
            throw Exception("User not found")
        }

        try {
            var querySnapshot = firestore.collection(AuthService.COLLECTION_USER)
                .document(user.uid)
                .collection(COLLECTION_HISTORY)
                .whereEqualTo("deleted", false)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(limit)

            if (searchKey != null) {
                querySnapshot = firestore.collection(AuthService.COLLECTION_USER)
                    .document(user.uid)
                    .collection(COLLECTION_HISTORY)
                    .whereEqualTo("deleted", false)
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .limit(limit)
            }

            if (lastVisible != null) {
                querySnapshot.startAfter(lastVisible)
            }

            val bookmarkSnapshot = querySnapshot.get().await()
            val movies = bookmarkSnapshot.documents.map { it.get("movie", Movie::class.java) }

            return Pair(movies, bookmarkSnapshot.documents.lastOrNull())
        } catch (e: Exception) {
            Timber.e(e)
            return Pair(emptyList(), null)
        }
    }

}