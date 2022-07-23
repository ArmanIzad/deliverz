package com.arman.deliverz.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "paging_table")
data class PagingKey(
    @PrimaryKey val id: Int,
    val nextKey: Int?,
    val previousKey: Int?
)
