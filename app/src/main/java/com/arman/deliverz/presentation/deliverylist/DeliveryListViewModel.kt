package com.arman.deliverz.presentation.deliverylist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arman.deliverz.domain.DeliveryRepositoryContract
import com.arman.deliverz.model.db.Delivery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DeliveryListViewModel @Inject constructor(private val repository: DeliveryRepositoryContract) :
    ViewModel() {

    private val _searchQuery = MutableLiveData("")

    fun getDeliveries(query: String? = null): Flow<PagingData<Delivery>> {
        if (query != null) {
            _searchQuery.value = query
        }
        return repository.getDeliveriesPaged(_searchQuery.value ?: "").cachedIn(viewModelScope)
    }
//
//    fun getDeliveries(): Flow<PagingData<Delivery>> {
//        return repository.getDeliveriesPaged(_searchQuery.value ?: "").cachedIn(viewModelScope)
//    }
}
