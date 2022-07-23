package com.arman.deliverz.model.api

import retrofit2.http.GET
import retrofit2.http.Query

interface DeliveryApi {
    @GET("deliveries")
    suspend fun getDeliveries(
        @Query("page") page: Int,
        @Query("limit") limit: Int = 21,
        @Query("description") description: String = ""
    ): List<DeliveryResponse>
}