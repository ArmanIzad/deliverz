package com.arman.deliverz.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.arman.deliverz.domain.DeliveryRepository.Companion.DEFAULT_PAGE_INDEX
import com.arman.deliverz.model.api.DeliveryApi
import com.arman.deliverz.model.db.Delivery
import com.arman.deliverz.model.toDelivery
import retrofit2.HttpException
import java.io.IOException

class DeliveryPagingSource(private val api: DeliveryApi, private val query: String) : PagingSource<Int, Delivery>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Delivery> {
        val page = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val response = api.getDeliveries(page, description = query).map { it.toDelivery() }
            LoadResult.Page(
                response, prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Delivery>): Int {
        return DEFAULT_PAGE_INDEX
    }
}