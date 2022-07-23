package com.arman.deliverz.domain

import androidx.paging.PagingSource
import com.arman.deliverz.FakeDeliveryApi
import com.arman.deliverz.FakeDeliveryData.getMockDeliveryResponses
import com.arman.deliverz.model.toDelivery
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(MockitoJUnitRunner::class)
class DeliveryPagingSourceTest {
    private val deliveryResponses = getMockDeliveryResponses()
    private val query = ""
    private val fakeApi = FakeDeliveryApi().apply {
        addResponse(deliveryResponses)
    }

    @Test
    fun loadCat_success() = runTest {
        val pagingSource = DeliveryPagingSource(fakeApi, query)
        assertEquals<PagingSource.LoadResult<out Any, out Any>>(
            expected = PagingSource.LoadResult.Page(
                data = listOf(
                    deliveryResponses[0].toDelivery(),
                    deliveryResponses[1].toDelivery(),
                    deliveryResponses[2].toDelivery()
                ),
                prevKey = null,
                nextKey = 2
            ),
            actual = pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 3,
                    placeholdersEnabled = false
                )
            ),
        )
    }

    @Test
    fun loadCat_failure() = runTest {
        fakeApi.failureMsg = "failure"
        val pagingSource = DeliveryPagingSource(fakeApi, query)

        val load = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 3,
                placeholdersEnabled = false
            )
        )

        assertTrue {
            load is PagingSource.LoadResult.Error && load.throwable is IOException
        }
    }
}
