package com.arman.deliverz.model.api

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class LocationResponse(@SerialName("lat") val lat: String,
                            @SerialName("lng") val long: String,
                            @SerialName("address") val address: String)