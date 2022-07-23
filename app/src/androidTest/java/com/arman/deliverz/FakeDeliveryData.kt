package com.arman.deliverz

import com.arman.deliverz.model.api.DeliveryResponse
import com.arman.deliverz.model.api.LocationResponse
import com.arman.deliverz.model.db.Delivery

object FakeDeliveryData {
    fun getMockDeliveryResponses(): List<DeliveryResponse> {
        return listOf(
            createDeliveryResponse("1"),
            createDeliveryResponse("2"),
            createDeliveryResponse("3")
        )
    }

    private fun createDeliveryResponse(id: String): DeliveryResponse =
        DeliveryResponse(id, "desc$id", "url$id", getMockLocation(id))

    private fun getMockLocation(id: String): LocationResponse =
        LocationResponse("${id}33.3", "${id}13.4", "address${id}")

    fun getMockDeliveries(): List<Delivery> {
        return listOf(
            createDelivery("1"),
            createDelivery("2"),
            createDelivery("3"),
            createDelivery("4")
        )
    }

    private fun createDelivery(id: String): Delivery =
        Delivery(id, "desc$id", "url$id", "lat$id", "lon$id", "Add$id")
}