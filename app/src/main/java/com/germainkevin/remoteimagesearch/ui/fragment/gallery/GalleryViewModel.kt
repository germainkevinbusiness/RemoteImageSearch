package com.germainkevin.remoteimagesearch.ui.fragment.gallery

import androidx.lifecycle.*
import androidx.paging.*
import com.germainkevin.remoteimagesearch.ui.fragment.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    repository: Repository,
    savedState: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val DEFAULT_SEARCH_QUERY = "current_query"
        private const val DEFAULT_QUERY_VALUE = "doggo"
    }

    // Default value of currentQuery at launch
    private val currentQuery: MutableLiveData<String> =
        savedState.getLiveData(DEFAULT_SEARCH_QUERY, DEFAULT_QUERY_VALUE)

    val getCurrentQuery: LiveData<String> = currentQuery

    fun searchPhotos(query: String) {
        currentQuery.value = query
    }

    val liveDataSearchResults = currentQuery.switchMap {
        repository.getSearchResults(it).cachedIn(viewModelScope)
    }
}