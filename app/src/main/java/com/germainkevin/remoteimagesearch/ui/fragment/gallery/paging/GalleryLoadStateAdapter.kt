package com.germainkevin.remoteimagesearch.ui.fragment.gallery.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.germainkevin.remoteimagesearch.databinding.LoadStatePagingPresenterBinding

/**
 * [LoadStateAdapter] used to display states inside the PagingAdapter
 * */
class GalleryLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<GalleryLoadStateAdapter.LoadStateViewHolder>() {

    inner class LoadStateViewHolder(private val itemBinding: LoadStatePagingPresenterBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(loadState: LoadState) {
            itemBinding.apply {
                loadStateProgressCircular.isVisible = loadState is LoadState.Loading
                loadStateRetryButton.isVisible = loadState !is LoadState.Loading
                loadStateErrorMsg.isVisible = loadState !is LoadState.Loading
            }
        }

        init {
            itemBinding.loadStateRetryButton.setOnClickListener { retry.invoke() }
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LoadStatePagingPresenterBinding.inflate(inflater, parent, false)
        return LoadStateViewHolder(binding)
    }
}