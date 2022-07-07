package com.example.shopingofmine.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shopingofmine.R
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.data.remote.ResultWrapper
import com.example.shopingofmine.databinding.FragmentHomeBinding
import com.example.shopingofmine.ui.adapters.ProductsPreviewRecyclerAdapter
import com.example.shopingofmine.ui.buildAndShowErrorDialog
import com.example.shopingofmine.ui.collectFlow
import com.example.shopingofmine.ui.rtl
import com.example.shopingofmine.ui.search.SearchFragmentDirections
import com.example.shopingofmine.ui.sharedviewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    val TAG = "ahmad"
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: HomeViewModel by viewModels()
    private var isDialogOnScreen = false
    private val loadedData = LoadedData()
    private lateinit var popularRecyclerAdapter: ProductsPreviewRecyclerAdapter
    private lateinit var topRatedRecyclerAdapter: ProductsPreviewRecyclerAdapter
    private lateinit var recentRecyclerAdapter: ProductsPreviewRecyclerAdapter
    private lateinit var homeSliderViewPagerAdapter: HomeSliderViewPagerAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        initSetSearchView()
        initializeRecyclerAdapters()
        initCollectFlows()
    }

    private fun initSetSearchView() {
        binding.searchView.rtl()
        binding.searchView.setOnQueryTextFocusChangeListener { _, _ ->
            if (findNavController().currentDestination?.id == R.id.homeFragment)
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
        }
    }

    private fun initializeRecyclerAdapters() {
        popularRecyclerAdapter = ProductsPreviewRecyclerAdapter(::onProductItemClick, ListType.POPULAR)
        topRatedRecyclerAdapter = ProductsPreviewRecyclerAdapter(::onProductItemClick, ListType.TOP_RATED)
        recentRecyclerAdapter = ProductsPreviewRecyclerAdapter(::onProductItemClick, ListType.NEWEST)
        binding.popularRecyclerView.adapter = popularRecyclerAdapter
        binding.topRatedRecyclerView.adapter = topRatedRecyclerAdapter
        binding.recentRecyclerView.adapter = recentRecyclerAdapter
    }

    private fun initCollectFlows() {
        collectFlow(viewModel.popularProducts) {
            when (it) {
                ResultWrapper.Loading -> {
                    doOnLoading()
                }
                is ResultWrapper.Success -> {
                    loadedData.isData1Successful = true
                    popularRecyclerAdapter.addStartItemAndSubmitList(it.value)
                    doOnSuccess()
                }
                is ResultWrapper.Error -> {
                    doOnError(it.message)
                }
            }
        }
        collectFlow(viewModel.topRatedProducts) {
            when (it) {
                ResultWrapper.Loading -> {
                    doOnLoading()
                }
                is ResultWrapper.Success -> {
                    loadedData.isData2Successful = true
                    topRatedRecyclerAdapter.addStartItemAndSubmitList(it.value)
                    doOnSuccess()
                }
                is ResultWrapper.Error -> {
                    doOnError(it.message)
                }
            }
        }
        collectFlow(viewModel.newProducts) {
            when (it) {
                ResultWrapper.Loading -> {
                    doOnLoading()
                }
                is ResultWrapper.Success -> {
                    loadedData.isData3Successful = true
                    recentRecyclerAdapter.addStartItemAndSubmitList(it.value)
                    doOnSuccess()
                }
                is ResultWrapper.Error -> {
                    doOnError(it.message)
                }
            }
        }
        collectFlow(viewModel.sliderProducts) {
            when (it) {
                ResultWrapper.Loading -> {
                    doOnLoading()
                }
                is ResultWrapper.Success -> {
                    loadedData.isData4Successful = true
                    val imageUrls = takeImageListFromProductList(it.value)
                    homeSliderViewPagerAdapter = HomeSliderViewPagerAdapter(imageUrls)
                    binding.viewPager.adapter = homeSliderViewPagerAdapter
                    doOnSuccess()
                    updateImageSlider()
                }
                is ResultWrapper.Error -> {
                    doOnError(it.message)
                }
            }
        }
    }

    private fun doOnLoading() {
        binding.loadingAnim.playAnimation()
        binding.loadingAnim.isGone = false
    }

    private fun doOnSuccess() {
        if (loadedData.isData1Successful && loadedData.isData2Successful && loadedData.isData3Successful && loadedData.isData4Successful) {
            binding.productsGroup.isGone = false
            binding.loadingAnim.pauseAnimation()
            binding.loadingAnim.isGone = true
        }
    }

    private fun doOnError(message: String?) {
        if (!isDialogOnScreen) {
            buildAndShowErrorDialog(message) {
                kotlin.run {
                    viewModel.getProducts()
                    viewModel.getSliderProducts()
                }
            }
            isDialogOnScreen = true
            binding.loadingAnim.pauseAnimation()
            binding.loadingAnim.isGone = true
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

    private fun updateImageSlider() {
        viewLifecycleOwner.lifecycleScope.launch {
            while (true) {
                delay(5000)
                if (binding.viewPager.currentItem + 1 != binding.viewPager.adapter?.itemCount)
                    binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
                else
                    binding.viewPager.setCurrentItem(0, true)
            }
        }
    }

    private fun onProductItemClick(product: ProductItem) {
        sharedViewModel.productItem = product
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(product.id))
    }

    private fun onStartOrEndItemClick(product: ProductItem) {
        findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToProductsFragment(null, ""))
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}