package com.hin.flixcomix.ui.search

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hin.flixcomix.data.entities.Country
import com.hin.flixcomix.data.entities.Genre
import com.hin.flixcomix.data.entities.Sort
import com.hin.flixcomix.ui.base.BaseViewModel
import com.hin.flixcomix.ui.search.model.FilterState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchViewModel : BaseViewModel() {
    private val _filterState = MutableLiveData(FilterState())
    val filterData = MediatorLiveData<FilterState>()

    private var debounceJob: Job? = null

    init {
        filterData.addSource(_filterState) { state ->
            debounceJob?.cancel()
            debounceJob = viewModelScope.launch {
                val delayTime = if (state.search != filterData.value?.search) 500L else 0L
                delay(delayTime)
                filterData.value = state
                if (delayTime > 0){
                    fetchSearchAndFilter()
                }
            }
        }
    }

    fun changeSort(item: Sort) {
        val current = _filterState.value ?: FilterState()
        _filterState.value = if (current.sort?.slug == item.slug) {
            current.copy(sort = null)
        } else {
            current.copy(sort = item)
        }
    }

    fun setSearch(search: String) {
        val current = _filterState.value ?: FilterState()
        _filterState.value = current.copy(search)
    }

    fun changeCountry(item: Country) {
        val current = _filterState.value ?: FilterState()
        _filterState.value = if (current.country?.slug == item.slug) {
            current.copy(country = null)
        } else {
            current.copy(country = item)
        }
    }

    fun changeGenre(item: Genre) {
        val current = _filterState.value ?: FilterState()
        _filterState.value = if (current.genre?.slug == item.slug) {
            current.copy(genre = null)
        } else {
            current.copy(genre = item)
        }
    }

    fun resetFilter() {
        _filterState.value = FilterState(search = _filterState.value?.search)
    }

    fun fetchSearchAndFilter() {
        Timber.i("fetchSearchAndFilter")
        _isLoading.value = true
        viewModelScope.launch {

        }
    }
}