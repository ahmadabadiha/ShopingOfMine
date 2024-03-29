package com.example.shopingofmine.ui.search

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shopingofmine.R
import com.example.shopingofmine.ShoppingNavDirections
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.data.remote.ResultWrapper
import com.example.shopingofmine.databinding.FragmentSearchBinding
import com.example.shopingofmine.ui.buildAndShowErrorDialog
import com.example.shopingofmine.ui.collectFlow
import com.example.shopingofmine.ui.rtl
import com.example.shopingofmine.ui.sharedviewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var searchRecyclerAdapter: SearchRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)
        initSetSearchView()
        initCollectFlows()
        searchRecyclerAdapter = SearchRecyclerAdapter(::onItemClick)
        binding.recyclerView.adapter = searchRecyclerAdapter
    }

    private fun initCollectFlows() {
        collectFlow(viewModel.searchResult) {
            when (it) {
                ResultWrapper.Loading -> {
                    if (binding.searchView.query.toString().isNotBlank()) {
                        binding.loadingAnim.playAnimation()
                        binding.loadingAnim.isGone = false
                    }
                }
                is ResultWrapper.Success -> {
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                    searchRecyclerAdapter.submitList(it.value)
                }
                is ResultWrapper.Error -> {
                    buildAndShowErrorDialog(it.message) { viewModel.searchProducts(binding.searchView.query.toString()) }
                }
            }
        }
    }

    private fun initSetSearchView() {
        binding.searchView.rtl()
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToProductsFragment(null, query))
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank()) {
                    binding.loadingAnim.isGone = false
                    binding.loadingAnim.playAnimation()
                    viewModel.searchProducts(newText)
                }
                return true
            }
        })
    }

    private fun onItemClick(product: ProductItem) {
        sharedViewModel.productItem = product
        findNavController().navigate(ShoppingNavDirections.actionGlobalProductDetailsFragment(product.id))
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}