package com.arman.deliverz.model.db

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "delivery_table")
data class Delivery(
    @PrimaryKey var id: String,
    var description: String,
    @ColumnInfo(name = "image_url") var  imageUrl: String,
    var lat: String,
    var long: String,
    var address: String
) {
    constructor(): this("", "", "", "", "", "")
}