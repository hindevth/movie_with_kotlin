package com.hin.movie.utils.extensions

import com.google.firebase.auth.FirebaseUser
import com.hin.movie.data.entities.User

fun FirebaseUser.toUser(): User {
    return User(
        uid = this.uid,
        name = this.displayName ?: "",
        email = this.email ?: "",
        photoUrl = this.photoUrl.toString()
    )
}