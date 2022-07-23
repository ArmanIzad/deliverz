package com.arman.deliverz.presentation.deliverylist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arman.deliverz.R
import com.arman.deliverz.databinding.DeliveryItemBinding
import com.arman.deliverz.model.db.Delivery
import com.arman.deliverz.presentation.widget.imageloader.ImageData
import com.arman.deliverz.presentation.widget.imageloader.ImageLoader

class DeliveryListAdapter(
    private val imageLoader: ImageLoader,
    private val onItemClick: (Delivery, View) -> Unit
) : PagingDataAdapter<Delivery, DeliveryListAdapter.DeliveryViewHolder>(
    diffCallback
) {
    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Delivery>() {
            override fun areItemsTheSame(oldItem: Delivery, newItem: Delivery): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Delivery, newItem: Delivery): Boolean =
                oldItem == newItem
        }
    }

    inner class DeliveryViewHolder(private val binding: DeliveryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(delivery: Delivery?) {
            if (delivery == null) {
                imageLoader.clearImage(binding.ivDelivery)
            } else {
                imageLoader.loadImage(
                    ImageData(
                        binding.root.context,
                        delivery.imageUrl,
                        R.drawable.ic_airport_24,
                        true,
                        binding.ivDelivery
                    )
                )
                binding.root.setOnClickListener { onItemClick(delivery, it) }
            }
            binding.rvDeliveryText.text = delivery?.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val binding =
            DeliveryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeliveryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }
}
