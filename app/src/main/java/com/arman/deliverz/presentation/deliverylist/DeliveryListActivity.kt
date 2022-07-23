package com.arman.deliverz.presentation.deliverylist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.arman.deliverz.R
import com.arman.deliverz.databinding.ActivityDeliveryListBinding
import com.arman.deliverz.presentation.datamodel.DeliveryParcelable
import com.arman.deliverz.presentation.datamodel.toDeliveryParcelable
import com.arman.deliverz.presentation.deliverydetails.DeliveryDetailsActivity
import com.arman.deliverz.presentation.deliverylist.adapter.DeliveryListAdapter
import com.arman.deliverz.presentation.deliverylist.adapter.DeliveryListLoadStateAdapter
import com.arman.deliverz.presentation.utils.isConnected
import com.arman.deliverz.presentation.utils.viewBinding
import com.arman.deliverz.presentation.widget.imageloader.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DeliveryListActivity : AppCompatActivity() {

    private val viewModel: DeliveryListViewModel by viewModels()
    private val binding by viewBinding(ActivityDeliveryListBinding::inflate)

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.delivery_list)
        initViews()
    }

    private fun initViews() {
        val deliveryAdapter =
            DeliveryListAdapter(
                imageLoader,
                onItemClick = { delivery, view ->
                    launchDeliveryDetails(
                        delivery.toDeliveryParcelable(),
                        view
                    )
                })
        binding.apply {
            rvDelivery.apply {
                adapter =
                    deliveryAdapter.withLoadStateFooter(DeliveryListLoadStateAdapter { deliveryAdapter.retry() })
                layoutManager = LinearLayoutManager(this@DeliveryListActivity)
                setHasFixedSize(true)
            }

            swipeRefresh.setOnRefreshListener { refreshList(deliveryAdapter) }

            getDeliveries(deliveryAdapter)
        }

        deliveryAdapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                rvDelivery.isVisible =
                    loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
                btnRetry.isVisible =
                    loadState.source.refresh is LoadState.Error && deliveryAdapter.itemCount == 0
                tvError.isVisible = loadState.source.refresh is LoadState.Error
                tvEmpty.isVisible =
                    loadState.source.refresh is LoadState.NotLoading && deliveryAdapter.itemCount == 0

                btnRetry.setOnClickListener {
                    deliveryAdapter.retry()
                }
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.length > 2) {
                    getDeliveries(deliveryAdapter, query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    getDeliveries(deliveryAdapter, "")
                }
                return true
            }
        })
    }

    private fun refreshList(adapter: DeliveryListAdapter) {
        if (isConnected) {
            adapter.refresh()
        } else {
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun getDeliveries(adapter: DeliveryListAdapter, query: String? = null) {
        lifecycleScope.launch {
            viewModel.getDeliveries(query).collectLatest {
                binding.swipeRefresh.isRefreshing = false
                adapter.submitData(it)
            }
        }
    }

    private fun launchDeliveryDetails(delivery: DeliveryParcelable, view: View) {
        val options =
            ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, view.transitionName)
                .toBundle()
        val intent = Intent(this, DeliveryDetailsActivity::class.java).apply {
            putExtra(DeliveryDetailsActivity.DELIVERY_KEY, delivery)
        }
        startActivity(intent, options)
    }
}