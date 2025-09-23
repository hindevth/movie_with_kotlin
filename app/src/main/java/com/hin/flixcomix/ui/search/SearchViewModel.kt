package com.hin.flixcomix.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hin.flixcomix.data.entities.Country
import com.hin.flixcomix.data.entities.Genre
import com.hin.flixcomix.data.entities.Sort
import com.hin.flixcomix.ui.search.model.FilterState
import timber.log.Timber

class SearchViewModel : ViewModel() {
    private val _filterState = MutableLiveData(FilterState())
    val filterData: LiveData<FilterState> = _filterState

    fun changeSort(item: Sort){
        val current = _filterState.value ?: FilterState()
        _filterState.value = if (current.sort?.slug == item.slug) {
            current.copy(sort = null)
        } else {
            current.copy(sort = item)
        }
    }

    fun changeCountry(item: Country){
        val current = _filterState.value ?: FilterState()
        _filterState.value = if (current.country?.slug == item.slug) {
            current.copy(country = null)
        } else {
            current.copy(country = item)
        }
    }

    fun changeGenre(item: Genre){
        val current = _filterState.value ?: FilterState()
        _filterState.value = if (current.genre?.slug == item.slug) {
            current.copy(genre = null)
        } else {
            current.copy(genre = item)
        }
    }

    fun resetFilter(){
        _filterState.value = FilterState()
    }
}