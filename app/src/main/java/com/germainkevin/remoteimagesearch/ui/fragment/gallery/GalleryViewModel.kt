package com.germainkevin.remoteimagesearch.ui.fragment.gallery

import androidx.lifecycle.*
import androidx.paging.*
import com.germainkevin.remoteimagesearch.api.UnsplashApi
import com.germainkevin.remoteimagesearch.api.data.UnsplashPhoto
import com.germainkevin.remoteimagesearch.ui.fragment.gallery.paging.DataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    unsplashApi: UnsplashApi,
    savedState: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val DEFAULT_SEARCH_QUERY = "current_query"
        private const val DEFAULT_QUERY_VALUE = "doggo"
    }

    // Reason for the creation of this repository: We need the GalleryViewModel's
    // unsplashApi passed as a parameter in the DataSource class.
    // We would need to make the GalleryViewModel's unsplashApi parameter private
    // for it to be accessible in getSearchResults()
    //
    // Hilt cannot inject private fields, thus we need to pass the injected
    // Unsplash api class as a variable to this repository
    // to make access to the unsplash api interface possible for the
    // DataSource class

    private inner class Repository(private val unsplashApi: UnsplashApi) {
        // Calls the PagingDataSource and gets its returned data in the Pager as Livedata
        fun getSearchResults(query: String) =
            Pager(
                config = PagingConfig(pageSize = 20, maxSize = 100, enablePlaceholders = false),
                pagingSourceFactory = { DataSource(unsplashApi, query) }
            ).liveData
    }

    private val repository = Repository(unsplashApi = unsplashApi)

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