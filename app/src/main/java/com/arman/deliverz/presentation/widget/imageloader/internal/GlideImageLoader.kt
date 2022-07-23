package com.arman.deliverz.presentation.widget.imageloader.internal

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.arman.deliverz.presentation.widget.imageloader.ImageData
import com.arman.deliverz.presentation.widget.imageloader.ImageLoader
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import javax.inject.Inject

class GlideImageLoader @Inject constructor() : ImageLoader {
    override fun loadImage(data: ImageData) {
        val listener = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                data.onLoadingFinished()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                data.onLoadingFinished()
                return false
            }
        }
        val requestOptions = RequestOptions.placeholderOf(data.placeholder)
            .dontTransform()

        Glide.with(data.context)
            .load(data.imageSource)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .fitCenter()
            .apply(requestOptions)
            .listener(listener)
            .sizeMultiplier(if (data.isThumbnail) 0.5f else 1.0f)
            .error(data.placeholder)
            .into(data.imageView)
    }

    override fun clearImage(imageView: ImageView) {
        Glide.with(imageView.context)
            .clear(imageView)
    }
}
