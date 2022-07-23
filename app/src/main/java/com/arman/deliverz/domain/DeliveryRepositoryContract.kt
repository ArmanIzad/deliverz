package com.arman.deliverz.domain

import androidx.paging.PagingData
import com.arman.deliverz.model.db.Delivery
import kotlinx.coroutines.flow.Flow

interface DeliveryRepositoryContract {
    /**
     * Gets list of deliveries from api or database
     * param query: the search term to use
     */
    fun getDeliveriesPaged(query: String): Flow<PagingData<Delivery>>
}