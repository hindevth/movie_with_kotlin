package com.hin.movie.data.remote.firebase

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.hin.movie.R
import com.hin.movie.utils.DeviceFingerprintStable
import com.hin.movie.utils.ProvideLogin
import com.hin.movie.utils.extensions.toMap
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.net.toUri
import com.hin.movie.data.entities.User
import com.hin.movie.data.local.UserDataSource

@Singleton
class AuthService @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firebaseMessaging: FirebaseMessaging,
    private val userDataSource: UserDataSource,
    @param:ApplicationContext private val context: Context
) {

    companion object {
        const val COLLECTION_USER = "users"
        const val COLLECTION_DEVICE = "devices"
    }

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

        val userOld = firestore.collection(COLLECTION_USER).document(authResult.user?.uid!!).get().await()

        if (userOld.exists()) {
            val userObject = userOld.toObject(User::class.java)
            if (userObject != null){
                userDataSource.saveUser(userObject)
            }
        }
        else {
            saveUser(authResult.user)
            val user = User(
                authResult.user?.uid!!,
                authResult.user?.displayName!!,
                authResult.user?.email!!,
                authResult.user?.photoUrl.toString(),

            )
            userDataSource.saveUser(user)
        }
        Result.success(authResult.user)
    } catch (e: androidx.credentials.exceptions.NoCredentialException) {
        Result.failure(Exception("Không tìm thấy tài khoản Google hợp lệ trên thiết bị."))
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun login(email: String, password: String): Result<FirebaseUser?> = try {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        saveUser(result.user, ProvideLogin.EMAIL)
        val userOld = firestore.collection(COLLECTION_USER).document(result.user?.uid!!).get().await()

        if (userOld.exists()) {
            val userObject = userOld.toObject(User::class.java)
            if (userObject != null){
                userDataSource.saveUser(userObject)
            }
        }else {
            saveUser(result.user, ProvideLogin.EMAIL)
            val user = User(
                result.user?.uid!!,
                result.user?.displayName!!,
                result.user?.email!!,
                result.user?.photoUrl.toString(),

                )
            userDataSource.saveUser(user)
        }
        Result.success(result.user)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun register(email: String, password: String): Result<FirebaseUser?> = try {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        saveUser(result.user, ProvideLogin.EMAIL)
        Result.success(result.user)
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun logout() {
        auth.signOut()
        userDataSource.clearUser()
    }

    suspend fun saveUser(user: FirebaseUser?, providerLogin: ProvideLogin = ProvideLogin.GOOGLE) {
        val deviceJson = DeviceFingerprintStable.getDeviceJson(context)
        val fingerprint = deviceJson.getString("fingerprint")
        val token = firebaseMessaging.token.await()
        val device = hashMapOf(
            "fingerprint" to fingerprint,
            "deviceInfo" to deviceJson.toMap(),
            "provideLogin" to providerLogin.name,
            "lastLogin" to System.currentTimeMillis(),
            "fcmToken" to token
        )

        val userMap = hashMapOf(
            "uid" to user?.uid,
            "email" to user?.email,
            "name" to user?.displayName,
            "photoUrl" to user?.photoUrl?.toString(),
            "phoneNumber" to user?.phoneNumber,
        )

        firestore.collection(COLLECTION_USER).document(user?.uid!!).set(userMap, SetOptions.merge()).await()

        firestore.collection(COLLECTION_USER)
            .document(user.uid)
            .collection(COLLECTION_DEVICE)
            .document(fingerprint)
            .set(device).await()
    }

    suspend fun updateProfile(name: String?, avatar: String?) {
        val currentUser = auth.currentUser
        val newProfile = userProfileChangeRequest {
            displayName = name ?: currentUser?.displayName
            photoUri = avatar?.toUri() ?: currentUser?.photoUrl
        }

        auth.currentUser?.updateProfile(newProfile)?.await()

        firestore
            .collection(COLLECTION_USER)
            .document(auth.currentUser?.uid!!)
            .update(
                mapOf(
                    "name" to (name ?: currentUser?.displayName),
                    "photoUrl" to (avatar ?: currentUser?.photoUrl)
                )
            )
            .await()
    }
}