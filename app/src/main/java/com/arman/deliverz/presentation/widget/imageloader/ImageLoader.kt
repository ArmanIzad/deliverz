package com.arman.deliverz.presentation.widget.imageloader

import android.widget.ImageView

interface ImageLoader {
    /**
     * loads an image into imageview
     */
    fun loadImage(data: ImageData)

    /**
     * clear an imageview
     */
    fun clearImage(imageView: ImageView)
}
