package com.hin.movie.ui.profile.data

import com.hin.movie.data.entities.Setting

data class UIState(
    val isLogoutSuccess: Boolean = false,
    val setting: Setting? = null
)
