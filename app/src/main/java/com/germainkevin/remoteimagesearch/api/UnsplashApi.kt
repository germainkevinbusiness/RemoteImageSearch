package com.germainkevin.remoteimagesearch.api

import com.germainkevin.remoteimagesearch.BuildConfig
import com.germainkevin.remoteimagesearch.api.data.UnsplashResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * API interface made to be manipulated by retrofit
 * */
interface UnsplashApi {

    companion object {
        const val BASE_URL = "https://api.unsplash.com/"
        const val PHOTOS_ROUTE = "search/photos"
        const val CLIENT_ID = BuildConfig.UNSPLASH_ACCESS_KEY
    }

    /**
     * @param query Search terms.
     * @param page Page number to retrieve. (Optional; default: 1)
     * @param perPage Number of items per page. (Optional; default: 10)
     * */
    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET(PHOTOS_ROUTE)
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): UnsplashResponse
}