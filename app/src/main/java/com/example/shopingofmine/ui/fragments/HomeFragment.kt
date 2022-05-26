package com.example.shopingofmine.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.shopingofmine.R
import com.example.shopingofmine.databinding.FragmentHomeBinding
import com.example.shopingofmine.model.serverdataclass.ProductItem
import com.example.shopingofmine.ui.viewmodels.HomeViewModel
import com.example.shopingofmine.ui.adapters.ProductsRecyclerAdapter
import com.example.shopingofmine.ui.viewmodels.SharedViewModel
import com.example.shopingofmine.util.ResultWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    val TAG = "ahmad"
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: HomeViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        val popularRecyclerAdapter = ProductsRecyclerAdapter(::onItemClick)
        val topRatedRecyclerAdapter = ProductsRecyclerAdapter(::onItemClick)
        val recentRecyclerAdapter = ProductsRecyclerAdapter(::onItemClick)
        binding.popularRecyclerView.adapter = popularRecyclerAdapter
        binding.topRatedRecyclerView.adapter = topRatedRecyclerAdapter
        binding.recentRecyclerView.adapter = recentRecyclerAdapter
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.popularProducts.collect {
                        when (it) {
                            ResultWrapper.Loading -> {
                                binding.loadingAnim.playAnimation()
                            }
                            is ResultWrapper.Success -> {
                                popularRecyclerAdapter.submitList(it.value)
                                binding.productsGroup.isGone = false
                                binding.loadingAnim.pauseAnimation()
                                binding.loadingAnim.isGone = true
                            }
                            is ResultWrapper.Error -> Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                launch {
                    viewModel.topRatedProducts.collect {
                        when (it) {
                            ResultWrapper.Loading -> {
                                binding.loadingAnim.playAnimation()
                            }
                            is ResultWrapper.Success -> {
                                topRatedRecyclerAdapter.submitList(it.value)
                                binding.productsGroup.isGone = false
                                binding.loadingAnim.pauseAnimation()
                                binding.loadingAnim.isGone = true
                            }
                            is ResultWrapper.Error -> Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                launch {
                    viewModel.newProducts.collect {
                        when (it) {
                            ResultWrapper.Loading -> {
                                binding.loadingAnim.playAnimation()
                            }
                            is ResultWrapper.Success -> {
                                recentRecyclerAdapter.submitList(it.value)
                                binding.productsGroup.isGone = false
                                binding.loadingAnim.pauseAnimation()
                                binding.loadingAnim.isGone = true
                            }
                            is ResultWrapper.Error -> Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun onItemClick(product: ProductItem) {
        sharedViewModel.productItem = product
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment())
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}