package com.arman.deliverz.model.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DeliveryDao {
    @Query("SELECT * FROM delivery_table where description LIKE :query ORDER BY id COLLATE NOCASE DESC ")
    fun getPaged(query: String): PagingSource<Int, Delivery>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deliveries: List<Delivery>)

    @Query("DELETE FROM delivery_table")
    suspend fun clear()
}