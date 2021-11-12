package com.germainkevin.remoteimagesearch.ui.fragment.gallery.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.germainkevin.remoteimagesearch.api.UnsplashApi
import com.germainkevin.remoteimagesearch.api.data.UnsplashPhoto
import retrofit2.HttpException
import java.io.IOException


private const val UNSPLASH_STARTING_PAGE_INDEX = 1

/**
 * @param unsplashApi Represents our backend service as it's built by Retrofit to
 * communicate with Unsplash API.
 * @param query Search terms inputted by the user.
 * */
class DataSource(private val unsplashApi: UnsplashApi, private val query: String) :
    PagingSource<Int, UnsplashPhoto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        // Start refresh at page 1 if undefined.
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        return try {
            val response = unsplashApi.searchPhotos(query, position, params.loadSize)
            val photos = response.results

            LoadResult.Page(
                data = photos,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            // Handle errors in this block and return LoadResult.Error if it is an
            // expected error (such as a network failure).
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UnsplashPhoto>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}