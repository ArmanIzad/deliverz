package com.arman.deliverz.presentation.widget.imageloader

import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes

/**
 * data class to represent image loading
 * @param context the context to load this image
 * @param imageSource the source of the image
 * @param placeholder the placeholder to be shown
 * @param isThumbnail specify whether this image is to be displayed in thumbnail
 * @param imageView the imageview to show the image
 * @param onLoadingFinished callback when image has been loaded
 *
 */
data class ImageData(
    var context: Context,
    var imageSource: Any,
    @DrawableRes var placeholder: Int,
    var isThumbnail: Boolean,
    var imageView: ImageView,
    val onLoadingFinished: () -> Unit = {}
)
