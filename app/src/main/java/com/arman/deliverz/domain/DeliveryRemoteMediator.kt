package com.arman.deliverz.domain

import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.arman.deliverz.domain.DeliveryRepository.Companion.DEFAULT_PAGE_INDEX
import com.arman.deliverz.model.api.DeliveryApi
import com.arman.deliverz.model.db.Delivery
import com.arman.deliverz.model.db.DeliverzDatabase
import com.arman.deliverz.model.db.PagingKey
import com.arman.deliverz.model.toDelivery
import retrofit2.HttpException
import java.io.IOException

class DeliveryRemoteMediator(
    private val deliveryApi: DeliveryApi,
    private val deliverzDatabase: DeliverzDatabase,
    private val query: String
) : RemoteMediator<Int, Delivery>() {

    private val deliveryDao = deliverzDatabase.deliveryDao()
    private val pagingKeyDao = deliverzDatabase.pagingKeyDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Delivery>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND -> {
                getPagingKey(state)?.previousKey
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                getPagingKey(state)?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        } ?: DEFAULT_PAGE_INDEX

        try {
            val nextPageKey = page + 1
            val searchResults = deliveryApi.getDeliveries(page, description = query)
            deliverzDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    deliveryDao.clear()
                    pagingKeyDao.clear()
                }

                val deliveries = searchResults.map {
                    it.toDelivery()
                }

                pagingKeyDao.insert(PagingKey(DEFAULT_PAGE_INDEX, nextPageKey + 1, nextPageKey))
                deliveryDao.insert(deliveries)
            }
            return MediatorResult.Success(endOfPaginationReached = searchResults.isEmpty())
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getPagingKey(state: PagingState<Int, Delivery>): PagingKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { deliverzDatabase.pagingKeyDao().getCurrentPage(DEFAULT_PAGE_INDEX) }
    }
}
