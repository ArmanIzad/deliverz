package com.arman.deliverz.model

import com.arman.deliverz.model.api.DeliveryResponse
import com.arman.deliverz.model.db.Delivery

fun DeliveryResponse.toDelivery(): Delivery =
    Delivery(id, description, imageUrl, location.lat, location.long, location.address)