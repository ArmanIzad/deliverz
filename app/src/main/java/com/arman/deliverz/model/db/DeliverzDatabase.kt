package com.arman.deliverz.model.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Delivery::class, PagingKey::class], version = 1, exportSchema = false)
abstract class DeliverzDatabase : RoomDatabase() {
    abstract fun deliveryDao(): DeliveryDao
    abstract fun pagingKeyDao(): PagingKeyDao
}