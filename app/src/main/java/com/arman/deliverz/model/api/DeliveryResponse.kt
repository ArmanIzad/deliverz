package com.arman.deliverz.model.api

import kotlinx.serialization.SerialName

//{
//    "id": "1",
//    "description": "Autem saepe necessitatibus id voluptatum est.",
//    "imageUrl": "http://loremflickr.com/640/480",
//    "location": {
//    "lat": "-62.1043",
//    "lng": "88.3203",
//    "address": "534 Hirthe Ranch"
//}
//}

@kotlinx.serialization.Serializable
data class DeliveryResponse(
    @SerialName("id") val id: String,
    @SerialName("description") val description: String,
    @SerialName("imageUrl") val imageUrl: String,
    @SerialName("location") val location: LocationResponse
)