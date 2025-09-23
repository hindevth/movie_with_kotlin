package com.hin.flixcomix.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel: ViewModel() {
    protected val _isLoading = MutableLiveData<Boolean>()
    protected val isLoading: LiveData<Boolean> = _isLoading
}