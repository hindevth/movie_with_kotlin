package com.hin.movie.utils.helper

import android.app.Activity
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.hin.movie.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GoogleAuthHelper @Inject constructor(@param:ApplicationContext private val appContext: Context) {

    suspend fun requestGoogleIdToken(activity: Activity): String {
        val credentialManager = CredentialManager.create(activity)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(activity.getString(R.string.default_web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val resultCredential = credentialManager.getCredential(activity, request)
        val googleIdToken = GoogleIdTokenCredential.createFrom(resultCredential.credential.data)
        return googleIdToken.idToken
    }
}