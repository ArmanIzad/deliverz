package com.arman.deliverz.di

import android.app.Application
import androidx.room.Room
import com.arman.deliverz.domain.DeliveryRepository
import com.arman.deliverz.domain.DeliveryRepositoryContract
import com.arman.deliverz.model.api.DeliveryApi
import com.arman.deliverz.model.db.DeliverzDatabase
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://60fe7e7e2574110017078616.mockapi.io/api/v1/")
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClientLogger(logger: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(logger)
            .build();

    @Provides
    @Singleton
    fun provideApiLogger(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideDeliveryApi(retrofit: Retrofit): DeliveryApi =
        retrofit.create(DeliveryApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(app: Application): DeliverzDatabase =
        Room.databaseBuilder(app, DeliverzDatabase::class.java, "deliverz_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun providesDeliveryRepository(
        deliveryApi: DeliveryApi,
        deliverzDatabase: DeliverzDatabase
    ): DeliveryRepositoryContract {
        return DeliveryRepository(deliveryApi, deliverzDatabase)
    }
}
