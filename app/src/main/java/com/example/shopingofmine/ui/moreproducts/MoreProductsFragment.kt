package com.example.shopingofmine.ui.moreproducts

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shopingofmine.R
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.data.remote.ResultWrapper
import com.example.shopingofmine.databinding.FragmentMoreProdcutsBinding
import com.example.shopingofmine.ui.adapters.MoreProductsRecyclerAdapter
import com.example.shopingofmine.ui.buildAndShowErrorDialog
import com.example.shopingofmine.ui.collectFlow
import com.example.shopingofmine.ui.sharedviewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreProductsFragment : Fragment(R.layout.fragment_more_prodcuts) {
    private var _binding: FragmentMoreProdcutsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MoreProductsViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var moreProductsRecyclerAdapter: MoreProductsRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMoreProdcutsBinding.bind(view)

        moreProductsRecyclerAdapter = MoreProductsRecyclerAdapter(::onItemClick)
        binding.recyclerView.adapter = moreProductsRecyclerAdapter

        collectFlow(viewModel.loadedProduct) {
            when (it) {
                ResultWrapper.Loading -> {
                    binding.loadingAnim.isGone = false
                    binding.loadingAnim.playAnimation()
                }
                is ResultWrapper.Success -> {
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                    moreProductsRecyclerAdapter.submitList(it.value)
                    binding.recyclerView.isGone = false
                }
                is ResultWrapper.Error -> {
                    buildAndShowErrorDialog(it.message) { viewModel.getProducts() }
                }
            }
        }
    }

    private fun onItemClick(product: ProductItem) {
        sharedViewModel.productItem = product
        findNavController().navigate(MoreProductsFragmentDirections.actionMoreProductsFragmentToProductDetailsFragment(product.id))
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}