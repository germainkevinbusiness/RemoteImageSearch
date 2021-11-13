package com.germainkevin.remoteimagesearch.ui.fragment

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.germainkevin.remoteimagesearch.api.UnsplashApi
import com.germainkevin.remoteimagesearch.ui.fragment.gallery.paging.DataSource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @param unsplashApi API interface made to be manipulated by retrofit
 * to construct our [UnsplashApi.searchPhotos] GET request, used in the [DataSource]
 * */
@Singleton
class Repository @Inject constructor(private val unsplashApi: UnsplashApi) {
    // Calls the PagingDataSource and gets its returned data in the Pager as Livedata
    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(pageSize = 20, maxSize = 100, enablePlaceholders = false),
            pagingSourceFactory = { DataSource(unsplashApi, query) }
        ).liveData
}