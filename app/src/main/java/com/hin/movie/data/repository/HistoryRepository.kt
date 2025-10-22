package com.hin.movie.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.hin.movie.data.remote.firebase.HistoryService
import javax.inject.Inject

class HistoryRepository @Inject constructor(private val historyService: HistoryService) {
    suspend fun getHistories(
        searchKey: String? = null,
        lastVisible: DocumentSnapshot? = null,
        limit: Long = 26
    ) = historyService.getHistories(searchKey, lastVisible, limit)

}