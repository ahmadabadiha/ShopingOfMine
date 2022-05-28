package com.example.shopingofmine.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.example.shopingofmine.ui.adapters.ProductsPreviewRecyclerAdapter
import com.example.shopingofmine.ui.sharedviewmodel.SharedViewModel
import com.example.shopingofmine.util.ResultWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    val TAG = "ahmad"
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var homeSliderViewPagerAdapter: HomeSliderViewPagerAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        val popularRecyclerAdapter = ProductsPreviewRecyclerAdapter(::onItemClick)
        val topRatedRecyclerAdapter = ProductsPreviewRecyclerAdapter(::onItemClick)
        val recentRecyclerAdapter = ProductsPreviewRecyclerAdapter(::onItemClick)
        binding.popularRecyclerView.adapter = popularRecyclerAdapter
        binding.topRatedRecyclerView.adapter = topRatedRecyclerAdapter
        binding.recentRecyclerView.adapter = recentRecyclerAdapter
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.popularProducts.collectLatest {
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
                            is ResultWrapper.Error -> {
                                val alertDialog: AlertDialog? = activity?.let {
                                    AlertDialog.Builder(it)
                                }?.setMessage(it.message)
                                    ?.setTitle("خطا")
                                    ?.setPositiveButton("تلاش مجدد") { _, _ ->
                                        viewModel.getProducts()
                                    }
                                    ?.setNegativeButton("انصراف") { _, _ ->
                                    }?.create()
                                alertDialog?.show()
                            }
                        }
                    }
                }
                launch {
                    viewModel.topRatedProducts.collectLatest {
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
                            is ResultWrapper.Error -> {
                                val alertDialog: AlertDialog? = activity?.let {
                                    AlertDialog.Builder(it)
                                }?.setMessage(it.message)
                                    ?.setTitle("خطا")
                                    ?.setPositiveButton("تلاش مجدد") { _, _ ->
                                        viewModel.getProducts()
                                    }
                                    ?.setNegativeButton("انصراف") { _, _ ->
                                    }?.create()
                                alertDialog?.show()
                            }
                        }
                    }
                }
                launch {
                    viewModel.newProducts.collectLatest {
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
                            is ResultWrapper.Error -> {
                                val alertDialog: AlertDialog? = activity?.let {
                                    AlertDialog.Builder(it)
                                }?.setMessage(it.message)
                                    ?.setTitle("خطا")
                                    ?.setPositiveButton("تلاش مجدد") { _, _ ->
                                        viewModel.getProducts()
                                    }
                                    ?.setNegativeButton("انصراف") { _, _ ->
                                    }?.create()
                                alertDialog?.show()
                            }
                        }
                    }
                }
                launch {
                    viewModel.sliderProducts.collectLatest {
                        when (it) {
                            ResultWrapper.Loading -> {
                                binding.loadingAnim.playAnimation()
                            }
                            is ResultWrapper.Success -> {
                                val imageUrls = takeImageListFromProductList(it.value)
                                homeSliderViewPagerAdapter = HomeSliderViewPagerAdapter(imageUrls)
                                binding.viewPager.adapter = homeSliderViewPagerAdapter
                                binding.productsGroup.isGone = false
                                binding.loadingAnim.pauseAnimation()
                                binding.loadingAnim.isGone = true
                            }
                            is ResultWrapper.Error -> {
                                val alertDialog: AlertDialog? = activity?.let {
                                    AlertDialog.Builder(it)
                                }?.setMessage(it.message)
                                    ?.setTitle("خطا")
                                    ?.setPositiveButton("تلاش مجدد") { _, _ ->
                                        viewModel.getProducts()
                                    }
                                    ?.setNegativeButton("انصراف") { _, _ ->
                                    }?.create()
                                alertDialog?.show()
                            }
                        }
                    }
                }
                updateImageSlider()
            }
        }
    }

    private fun takeImageListFromProductList(productList: List<ProductItem>): List<String> {
        val imageUrls = mutableListOf<String>()
        val imageList = productList.flatMap {
            it.images
        }
        imageList.forEach {
            imageUrls.add(it.src)
        }
        return imageUrls
    }

    private suspend fun updateImageSlider() {
        while (true) {
            delay(5000)
            if (binding.viewPager.currentItem + 1 != binding.viewPager.adapter?.itemCount)
                binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
            else
                binding.viewPager.setCurrentItem(0, true)
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