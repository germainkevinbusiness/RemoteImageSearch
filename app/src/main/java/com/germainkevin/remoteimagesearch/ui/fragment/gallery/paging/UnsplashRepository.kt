package com.germainkevin.remoteimagesearch.ui.fragment.gallery.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.germainkevin.remoteimagesearch.api.UnsplashApi
import javax.inject.Inject
import javax.inject.Singleton

//@Singleton
//class UnsplashRepository @Inject constructor(private val unsplashApi: UnsplashApi) {
//
//    fun getSearchResults(query: String) =
//        Pager(
//            config = PagingConfig(pageSize = 20, maxSize = 100, enablePlaceholders = false),
//            pagingSourceFactory = { DataSource(unsplashApi, query) }
//        ).liveData
//}