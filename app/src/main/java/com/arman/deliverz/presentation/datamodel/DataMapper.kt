package com.arman.deliverz.presentation.datamodel

import com.arman.deliverz.model.api.DeliveryResponse
import com.arman.deliverz.model.db.Delivery

fun Delivery.toDeliveryParcelable(): DeliveryParcelable =
    DeliveryParcelable(id, description, imageUrl, lat, long, address)