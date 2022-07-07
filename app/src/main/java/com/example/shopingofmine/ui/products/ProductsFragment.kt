package com.example.shopingofmine.ui.products

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.shopingofmine.R
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.data.remote.ResultWrapper
import com.example.shopingofmine.databinding.FragmentProductsBinding
import com.example.shopingofmine.ui.adapters.ProductsRecyclerAdapter
import com.example.shopingofmine.ui.buildAndShowErrorDialog
import com.example.shopingofmine.ui.collectFlow
import com.example.shopingofmine.ui.sharedviewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment(R.layout.fragment_products) {
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private val navArgs: ProductsFragmentArgs by navArgs()
    private val viewModel: ProductsViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var productsRecyclerAdapter: ProductsRecyclerAdapter
    private var category: String? = null
    private var query: String? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductsBinding.bind(view)
        category = navArgs.category
        query = navArgs.query
        productsRecyclerAdapter = ProductsRecyclerAdapter(::onItemClick)
        binding.recyclerView.adapter = productsRecyclerAdapter
        initCollectFlows()
        setDropDownItems()
        setSorting()
    }

    private fun setSorting() {
        binding.sortET.addTextChangedListener {
            binding.loadedGroup.isGone = true
            binding.loadingAnim.playAnimation()
            binding.loadingAnim.isGone = false
            when (it.toString()) {
                "امتیاز-صعودی" -> {
                    if (category != null) viewModel.getProductsByCategory(categoryId = category!!, orderBy = "rating", order = "asc")
                    else viewModel.searchProducts(query = query!!, orderBy = "rating", order = "asc")
                }
                "امتیاز-نزولی" -> {
                    if (category != null) viewModel.getProductsByCategory(categoryId = category!!, orderBy = "rating", order = "desc")
                    else viewModel.searchProducts(query = query!!, orderBy = "rating", order = "desc")
                }
                "بازدید-صعودی" -> {
                    if (category != null) viewModel.getProductsByCategory(categoryId = category!!, orderBy = "popularity", order = "asc")
                    else viewModel.searchProducts(query = query!!, orderBy = "popularity", order = "asc")
                }
                "بازدید-نزولی" -> {
                    if (category != null) viewModel.getProductsByCategory(categoryId = category!!, orderBy = "popularity", order = "desc")
                    else viewModel.searchProducts(query = query!!, orderBy = "popularity", order = "desc")
                }
                "قیمت-صعودی" -> {
                    if (category != null) viewModel.getProductsByCategory(categoryId = category!!, orderBy = "price", order = "asc")
                    else viewModel.searchProducts(query = query!!, orderBy = "price", order = "asc")
                }
                "قیمت-نزولی" -> {
                    if (category != null) viewModel.getProductsByCategory(categoryId = category!!, orderBy = "price", order = "desc")
                    else viewModel.searchProducts(query = query!!, orderBy = "price", order = "desc")
                }
                "تاریخ-صعودی" -> {
                    if (category != null) viewModel.getProductsByCategory(categoryId = category!!, orderBy = "date", order = "asc")
                    else viewModel.searchProducts(query = query!!, orderBy = "date", order = "asc")
                }
                "تاریخ-نزولی" -> {
                    if (category != null) viewModel.getProductsByCategory(categoryId = category!!, orderBy = "date", order = "desc")
                    else viewModel.searchProducts(query = query!!, orderBy = "date", order = "desc")
                }
            }
        }
    }

    private fun initCollectFlows() {
        collectFlow(viewModel.categorizedProducts) {
            when (it) {
                ResultWrapper.Loading -> {
                    binding.loadingAnim.playAnimation()
                }
                is ResultWrapper.Success -> {
                    productsRecyclerAdapter.submitList(it.value)
                    binding.loadedGroup.isGone = false
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                }
                is ResultWrapper.Error -> {
                    buildAndShowErrorDialog(message = it.message) { viewModel.getProductsByCategory(category!!) }
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                }
            }
        }
        collectFlow(viewModel.searchResult) {
            when (it) {
                ResultWrapper.Loading -> {
                    binding.loadingAnim.isGone = false
                    binding.loadingAnim.playAnimation()
                }
                is ResultWrapper.Success -> {
                    productsRecyclerAdapter.submitList(it.value)
                    binding.loadedGroup.isGone = false
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                }
                is ResultWrapper.Error -> {
                    buildAndShowErrorDialog(it.message) { viewModel.searchProducts(query!!) }
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                }
            }
        }
    }

    private fun onItemClick(product: ProductItem) {
        sharedViewModel.productItem = product
        findNavController().navigate(ProductsFragmentDirections.actionProductsFragmentToProductDetailsFragment(product.id))
    }

    private fun setDropDownItems() {
        val items =
            listOf("تاریخ-نزولی", "تاریخ-صعودی", "قیمت-نزولی", "قیمت-صعودی", "بازدید-نزولی", "بازدید-صعودی", "امتیاز-نزولی", "امتیاز-صعودی")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        binding.sortET.setAdapter(adapter)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}