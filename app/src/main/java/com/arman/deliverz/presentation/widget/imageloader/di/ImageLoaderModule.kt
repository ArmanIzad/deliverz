package com.arman.deliverz.presentation.widget.imageloader.di

import com.arman.deliverz.presentation.widget.imageloader.ImageLoader
import com.arman.deliverz.presentation.widget.imageloader.internal.GlideImageLoader
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class ImageLoaderModule {
    @Binds
    abstract fun bindImageLoader(
        glideImageLoader: GlideImageLoader
    ): ImageLoader
}
