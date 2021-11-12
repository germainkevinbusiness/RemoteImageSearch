package com.germainkevin.remoteimagesearch.api.data

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.parcelize.Parcelize

/**
 * Represents the object returned by the unsplash api on a
 * GET [unsplash api web address]/search/photos request
 * */
@Parcelize
data class UnsplashPhoto(
    val id: String,
    val description: String?,
    val urls: UnsplashPhotoUrls,
    val user: UnsplashUser
) : Parcelable {

    @Parcelize
    data class UnsplashPhotoUrls(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String
    ) : Parcelable

    @Parcelize
    data class UnsplashUser(
        val name: String,
        val username: String
    ) : Parcelable {
        val attributionUrl
            get() = "https://unsplash.com/$username?utm_source=ImageSearchApp&utm_medium=referral"
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<UnsplashPhoto>() {
            override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: UnsplashPhoto,
                newItem: UnsplashPhoto
            ): Boolean = oldItem == newItem
        }
    }
}