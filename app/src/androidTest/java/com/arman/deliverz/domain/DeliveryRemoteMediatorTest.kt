package com.arman.deliverz.domain

import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arman.deliverz.FakeDeliveryApi
import com.arman.deliverz.FakeDeliveryData.getMockDeliveryResponses
import com.arman.deliverz.model.db.Delivery
import com.arman.deliverz.model.db.DeliverzDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class DeliveryRemoteMediatorTest {
    private val deliveryResponses = getMockDeliveryResponses()
    private val fakeApi = FakeDeliveryApi()
    private val query = ""

    private lateinit var mockDb: DeliverzDatabase

    @Before
    fun setup() {
        mockDb = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DeliverzDatabase::class.java
        ).build()
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
        fakeApi.failureMsg = null
        fakeApi.clear()
    }

    @Test
    fun refreshLoad_success_with_data() = runBlocking {
        fakeApi.apply {
            addResponse(deliveryResponses)
        }

        val remoteMediator = DeliveryRemoteMediator(
            fakeApi,
            mockDb,
            query
        )
        val pagingState = PagingState<Int, Delivery>(
            listOf(),
            null,
            PagingConfig(20),
            20
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue { result is RemoteMediator.MediatorResult.Success }
        assertFalse { (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached }
    }

    @Test
    fun refreshLoad_success_no_data() = runBlocking {
        val remoteMediator = DeliveryRemoteMediator(
            fakeApi,
            mockDb,
            query
        )
        val pagingState = PagingState<Int, Delivery>(
            listOf(),
            null,
            PagingConfig(20),
            20
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue { result is RemoteMediator.MediatorResult.Success }
        assertTrue { (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached }
    }

    @Test
    fun refreshLoad_error() = runBlocking {
        fakeApi.failureMsg = "failure"
        val remoteMediator = DeliveryRemoteMediator(
            fakeApi,
            mockDb,
            query
        )
        val pagingState = PagingState<Int, Delivery>(
            listOf(),
            null,
            PagingConfig(20),
            20
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue { result is RemoteMediator.MediatorResult.Error }
    }
}
