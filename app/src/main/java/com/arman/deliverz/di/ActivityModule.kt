package com.arman.deliverz.di

import com.arman.deliverz.domain.DeliveryRepository
import com.arman.deliverz.domain.DeliveryRepositoryContract
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class DeliveryRepositoryModule

@InstallIn(ActivityComponent::class)
@Module
abstract class ActivityModule {
    @DeliveryRepositoryModule
    @Singleton
    @Binds
    abstract fun bindDeliveryRepository(repository: DeliveryRepository): DeliveryRepositoryContract
}
