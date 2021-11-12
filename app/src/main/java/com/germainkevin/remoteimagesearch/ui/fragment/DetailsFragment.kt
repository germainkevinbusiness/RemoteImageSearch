package com.germainkevin.remoteimagesearch.ui.fragment

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.germainkevin.remoteimagesearch.R
import com.germainkevin.remoteimagesearch.api.UnsplashApi
import com.germainkevin.remoteimagesearch.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val photo = args.photo
            Glide.with(this@DetailsFragment).load(photo.urls.regular)
                .error(R.drawable.ic_error)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressCircular.isVisible = false
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressCircular.isVisible = false
                        unsplashDescription.isVisible = photo.description != null
                        unsplashAuthor.isVisible = true
                        return false
                    }

                })
                .into(unsplashPhoto)

            unsplashDescription.text = photo.description

            val uri = Uri.parse(photo.user.attributionUrl)
            val intent = Intent(Intent.ACTION_VIEW, uri)

            unsplashAuthor.apply {
                text = "Photo by : ${photo.user.name} on ${UnsplashApi.BASE_URL}"

                setOnClickListener { context.startActivity(intent) }

                paint.isUnderlineText = true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
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