package com.arman.deliverz.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.arman.deliverz.model.api.DeliveryApi
import com.arman.deliverz.model.db.Delivery
import com.arman.deliverz.model.db.DeliverzDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class DeliveryRepository @Inject constructor(
    private val deliveryApi: DeliveryApi,
    private val deliverzDatabase: DeliverzDatabase
) : DeliveryRepositoryContract {
    companion object {
        const val DEFAULT_PAGE_INDEX = 1
        const val DEFAULT_PAGE_SIZE = 21
    }

    override fun getDeliveriesPaged(query: String): Flow<PagingData<Delivery>> =
        Pager(
            config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = DeliveryRemoteMediator(deliveryApi, deliverzDatabase, query),
            pagingSourceFactory = { DeliveryPagingSource(deliveryApi, query) }
        ).flow
}