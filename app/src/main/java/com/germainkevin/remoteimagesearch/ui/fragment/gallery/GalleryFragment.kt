package com.germainkevin.remoteimagesearch.ui.fragment.gallery

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.germainkevin.remoteimagesearch.R
import com.germainkevin.remoteimagesearch.databinding.FragmentGalleryBinding
import com.germainkevin.remoteimagesearch.ui.activity.MainActivity
import com.germainkevin.remoteimagesearch.ui.fragment.gallery.paging.GalleryLoadStateAdapter
import com.germainkevin.remoteimagesearch.ui.fragment.gallery.paging.GalleryPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GalleryFragment : Fragment() {
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<GalleryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val galleryPagingAdapter = GalleryPagingAdapter {
            val action = GalleryFragmentDirections.actionGalleryFragmentToDetailsFragment(it)
            findNavController().navigate(action)
        }
        with(binding) {
            val headerLoadStateAdapter = GalleryLoadStateAdapter(galleryPagingAdapter::retry)
            val footerLoadStateAdapter = GalleryLoadStateAdapter(galleryPagingAdapter::retry)
            galleryRecyclerView.adapter =
                galleryPagingAdapter.withLoadStateHeaderAndFooter(
                    header = headerLoadStateAdapter,
                    footer = footerLoadStateAdapter
                )
            retryButton.setOnClickListener { galleryPagingAdapter.retry() }
            Pair(binding, galleryPagingAdapter).observeAndCollect()
        }
    }

    private fun Pair<FragmentGalleryBinding, GalleryPagingAdapter>.observeAndCollect() {

        viewModel.getCurrentQuery.observe(viewLifecycleOwner) {
            (activity as MainActivity).supportActionBar?.title =
                "Results for \"${viewModel.getCurrentQuery.value}\""
        }
        viewModel.liveDataSearchResults.observe(viewLifecycleOwner) { pagingData ->
            second.submitData(viewLifecycleOwner.lifecycle, pagingData)
        }
        lifecycleScope.launch {
            second.loadStateFlow.collectLatest { loadState ->
                first.progressCircular.isVisible = loadState.source.refresh is LoadState.Loading

                if (loadState.source.refresh is LoadState.Error) {
                    first.galleryRecyclerView.isVisible = false
                    first.errorMsg.text = getString(R.string.results_could_not_be_loaded)
                    first.errorMsg.isVisible = true
                    first.retryButton.isVisible = true
                } else {
                    first.galleryRecyclerView.isVisible = true
                    first.errorMsg.isVisible = false
                    first.retryButton.isVisible = false
                }

                // no results
                if (loadState.source.refresh is LoadState.NotLoading
                    && loadState.append.endOfPaginationReached
                    && second.itemCount < 1 // no results
                ) {
                    first.galleryRecyclerView.isVisible = false
                    first.errorMsg.text = getString(R.string.no_results_found_for_this_query)
                    first.errorMsg.isVisible = true
                    first.retryButton.isVisible = true
                } else {
                    first.galleryRecyclerView.isVisible = true
                    first.errorMsg.isVisible = false
                    first.retryButton.isVisible = false
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_gallery, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.galleryRecyclerView.scrollToPosition(0)
                    viewModel.searchPhotos(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                findNavController().navigate(R.id.action_galleryFragment_to_settingsFragment)
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}