package com.hin.movie.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class FirebaseMessagingService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.i("New Token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Timber.i("New Message from: ${message.from}")
        Timber.i("New Message data: ${message.data}")
        Timber.i("New Message notification title: ${message.notification?.title}")
        Timber.i("New Message notification body: ${message.notification?.body}")
    }
}