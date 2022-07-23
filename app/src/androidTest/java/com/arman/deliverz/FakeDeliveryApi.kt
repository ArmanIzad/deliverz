package com.arman.deliverz

import com.arman.deliverz.model.api.DeliveryApi
import com.arman.deliverz.model.api.DeliveryResponse
import java.io.IOException

class FakeDeliveryApi : DeliveryApi {
    private val model = mutableListOf<DeliveryResponse>()
    var failureMsg: String? = null
    fun addResponse(response: List<DeliveryResponse>) {
        model.addAll(response)
    }

    fun clear() {
        model.clear()
    }

    override suspend fun getDeliveries(
        page: Int,
        limit: Int,
        description: String
    ): List<DeliveryResponse> {
        failureMsg?.let {
            throw IOException(it)
        }
        return model
    }
}
