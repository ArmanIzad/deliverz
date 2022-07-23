package com.arman.deliverz.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PagingKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pagingKey: PagingKey)

    @Query("SELECT * FROM paging_table WHERE id = :id")
    suspend fun getCurrentPage(id: Int): PagingKey?

    @Query("DELETE FROM paging_table")
    suspend fun clear()
}
