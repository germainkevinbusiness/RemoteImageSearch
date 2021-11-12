package com.germainkevin.remoteimagesearch.ui.fragment.gallery.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.germainkevin.remoteimagesearch.R
import com.germainkevin.remoteimagesearch.api.data.UnsplashPhoto
import com.germainkevin.remoteimagesearch.databinding.UnsplashPhotoRowBinding

/**
 * [RecyclerView.Adapter] for pagination in the GalleryFragment
 * */
class GalleryPagingAdapter(
    private val onItemClickListener: (UnsplashPhoto) -> Unit
) : PagingDataAdapter<UnsplashPhoto,
        GalleryPagingAdapter.GalleryViewHolder>(UnsplashPhoto.diffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = UnsplashPhotoRowBinding.inflate(inflater, parent, false)
        return GalleryViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val currentRow = getItem(position)
        if (currentRow != null) {
            holder.bind(currentRow)
        }
    }

    inner class GalleryViewHolder(
        private val itemBinding: UnsplashPhotoRowBinding,
        vHItemClickedListener: (UnsplashPhoto) -> Unit
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        var actualUnsplashPhoto: UnsplashPhoto? = null

        fun bind(photo: UnsplashPhoto) {
            actualUnsplashPhoto = photo
            itemBinding.apply {
                Glide.with(itemView).load(photo.urls.regular).centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error).into(unsplashPhoto)

                unsplashAuthor.text = photo.user.username
            }
        }

        init {
            itemBinding.root.setOnClickListener {
                actualUnsplashPhoto?.let { vHItemClickedListener(it) }
            }
        }
    }
}