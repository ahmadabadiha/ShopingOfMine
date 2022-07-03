package com.example.shopingofmine.ui.home

import android.os.Bundle
import android.util.Log
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
    private val resultList = mutableListOf<ResultWrapper<List<ProductItem>>>()
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

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank()) {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment(newText.toString()))
                    binding.searchView.setQuery("", false)
                }
                return true
            }
        })
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
                    binding.loadingAnim.playAnimation()
                    binding.loadingAnim.isGone = false
                }
                is ResultWrapper.Success -> {
                    Log.d(TAG, "initCollectFlows: 1")
                    popularRecyclerAdapter.addStartItemAndSubmitList(it.value)
                    binding.productsGroup.isGone = false
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                    resultList.add(it)
                }
                is ResultWrapper.Error -> {
                    resultList.add(it)
                    buildAndShowErrorDialog(it.message) { viewModel.getProducts() }
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                }
            }
        }
        collectFlow(viewModel.topRatedProducts) {
            when (it) {
                ResultWrapper.Loading -> {
                    binding.loadingAnim.playAnimation()
                    binding.loadingAnim.isGone = false
                }
                is ResultWrapper.Success -> {
                    resultList.add(it)
                    Log.d(TAG, "initCollectFlows: 2")
                    topRatedRecyclerAdapter.addStartItemAndSubmitList(it.value)
                    binding.productsGroup.isGone = false
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                }
                is ResultWrapper.Error -> {
                    resultList.add(it)
                    buildAndShowErrorDialog(it.message) { viewModel.getProducts() }
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                }
            }
        }
        collectFlow(viewModel.newProducts) {
            when (it) {
                ResultWrapper.Loading -> {
                    binding.loadingAnim.playAnimation()
                    binding.loadingAnim.isGone = false
                }
                is ResultWrapper.Success -> {
                    resultList.add(it)
                    Log.d(TAG, "initCollectFlows: 3")
                    recentRecyclerAdapter.addStartItemAndSubmitList(it.value)
                    binding.productsGroup.isGone = false
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                }
                is ResultWrapper.Error -> {
                    resultList.add(it)
                    buildAndShowErrorDialog(it.message) { viewModel.getProducts() }
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                }
            }
        }
        collectFlow(viewModel.sliderProducts) {
            when (it) {
                ResultWrapper.Loading -> {
                    binding.loadingAnim.playAnimation()
                    binding.loadingAnim.isGone = false
                }
                is ResultWrapper.Success -> {
                    Log.d(TAG, "initCollectFlows: 4")
                    val imageUrls = takeImageListFromProductList(it.value)
                    homeSliderViewPagerAdapter = HomeSliderViewPagerAdapter(imageUrls)
                    binding.viewPager.adapter = homeSliderViewPagerAdapter
                    binding.productsGroup.isGone = false
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                    updateImageSlider()
                }
                is ResultWrapper.Error -> {
                    buildAndShowErrorDialog(it.message) { viewModel.getSliderProducts() }
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                }
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