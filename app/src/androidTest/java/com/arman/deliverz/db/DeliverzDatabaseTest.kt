package com.arman.deliverz.db

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arman.deliverz.FakeDeliveryData
import com.arman.deliverz.model.db.DeliveryDao
import com.arman.deliverz.model.db.DeliverzDatabase
import com.arman.deliverz.model.db.PagingKey
import com.arman.deliverz.model.db.PagingKeyDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class DeliverzDatabaseTest {

    private lateinit var deliveryDao: DeliveryDao
    private lateinit var pagingKeyDao: PagingKeyDao
    private lateinit var db: DeliverzDatabase
    private val query = "desc"

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DeliverzDatabase::class.java
        ).build()
        deliveryDao = db.deliveryDao()
        pagingKeyDao = db.pagingKeyDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.clearAllTables()
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun deliveryDao_getPaged_empty() = runBlocking {
        val paging = deliveryDao.getPaged(query)
        assertEquals<PagingSource.LoadResult<out Any, out Any>>(
            expected = PagingSource.LoadResult.Page(
                data = listOf(),
                prevKey = null,
                nextKey = null,
                itemsBefore = 0,
                itemsAfter = 0
            ),
            actual = paging.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 3,
                    placeholdersEnabled = false
                )
            ),
        )
    }

    @Test
    @Throws(IOException::class)
    fun deliveryDao_clear_success() = runBlocking {
        val deliveries = FakeDeliveryData.getMockDeliveries()
        deliveryDao.insert(deliveries)
        deliveryDao.clear()
        val paging = deliveryDao.getPaged(query)
        assertEquals<PagingSource.LoadResult<out Any, out Any>>(
            expected = PagingSource.LoadResult.Page(
                data = listOf(),
                prevKey = null,
                nextKey = null,
                itemsBefore = 0,
                itemsAfter = 0
            ),
            actual = paging.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 3,
                    placeholdersEnabled = false
                )
            ),
        )
    }

    @Test
    @Throws(IOException::class)
    fun pagingKeyDao_get_valid_success() = runBlocking {
        val nextKey = 1
        val previousKey = 2
        val key = createMockPagingKey(nextKey, previousKey)
        pagingKeyDao.insert(key)

        val pagingKey = pagingKeyDao.getCurrentPage(0)

        assert(pagingKey?.nextKey == nextKey && pagingKey.previousKey == previousKey)
    }

    @Test
    @Throws(IOException::class)
    fun pagingKeyDao_get_empty_success() = runBlocking {
        val pagingKey = pagingKeyDao.getCurrentPage(10)
        assert(pagingKey == null)
    }

    @Test
    @Throws(IOException::class)
    fun pagingKeyDao_clear_success() = runBlocking {
        val key = createMockPagingKey(1, 2)
        pagingKeyDao.insert(key)
        pagingKeyDao.clear()

        val pagingKey = pagingKeyDao.getCurrentPage(0)

        assert(pagingKey == null)
    }

    private fun createMockPagingKey(nextKey: Int?, previousKey: Int): PagingKey {
        return PagingKey(0, nextKey, previousKey)
    }
}