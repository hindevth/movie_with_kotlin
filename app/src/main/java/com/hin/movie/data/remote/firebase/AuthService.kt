package com.hin.movie.data.remote.firebase

import android.app.Activity
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.hin.movie.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthService @Inject constructor(
    private val auth: FirebaseAuth ,
) {

    suspend fun loginGoogle(activity: Activity): Result<FirebaseUser?> = try {
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

        val credential = GoogleAuthProvider.getCredential(googleIdToken.idToken, null)
        val authResult = auth.signInWithCredential(credential).await()

        Result.success(authResult.user)
    } catch (e: androidx.credentials.exceptions.NoCredentialException) {
        Result.failure(Exception("Không tìm thấy tài khoản Google hợp lệ trên thiết bị."))
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun login(email: String, password: String): Result<FirebaseUser?> = try {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        Result.success(result.user)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun register(email: String, password: String): Result<FirebaseUser?> = try {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        Result.success(result.user)
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun logout() {
        auth.signOut()
    }
}