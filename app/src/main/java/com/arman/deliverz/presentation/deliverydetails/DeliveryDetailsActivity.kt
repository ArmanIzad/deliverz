package com.arman.deliverz.presentation.deliverydetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arman.deliverz.R
import com.arman.deliverz.databinding.ActivityDeliveryDetailsBinding
import com.arman.deliverz.presentation.datamodel.DeliveryParcelable
import com.arman.deliverz.presentation.utils.viewBinding
import com.arman.deliverz.presentation.widget.imageloader.ImageData
import com.arman.deliverz.presentation.widget.imageloader.ImageLoader
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DeliveryDetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    @Inject
    lateinit var imageLoader: ImageLoader

    private lateinit var deliveryParcelable: DeliveryParcelable
    private val binding by viewBinding(ActivityDeliveryDetailsBinding::inflate)

    companion object {
        const val DELIVERY_KEY = "delivery_parcelable"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.delivery_details)
        supportPostponeEnterTransition()
        initViews()
    }

    private fun initViews() {
        intent.getParcelableExtra<DeliveryParcelable>(DELIVERY_KEY)?.let {
            deliveryParcelable = it
            binding.apply {
                imageLoader.loadImage(
                    ImageData(
                        this@DeliveryDetailsActivity,
                        deliveryParcelable.imageUrl,
                        R.drawable.ic_airport_24,
                        false,
                        binding.cvDelivery.ivDelivery
                    ) {
                        supportStartPostponedEnterTransition()
                    })
                binding.cvDelivery.rvDeliveryText.text = deliveryParcelable.description
            }
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val deliveryLocation = LatLng(deliveryParcelable.lat.toDouble(), deliveryParcelable.long.toDouble())
        googleMap.addMarker(
            MarkerOptions()
                .position(deliveryLocation)
                .title(deliveryParcelable.address)
        )
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(deliveryLocation, 15f))
    }
}
