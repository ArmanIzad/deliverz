package com.arman.deliverz.presentation.datamodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeliveryParcelable(val id: String,
                              val description: String,
                              val imageUrl: String,
                              val lat: String,
                              val long: String,
                              val address: String) : Parcelable