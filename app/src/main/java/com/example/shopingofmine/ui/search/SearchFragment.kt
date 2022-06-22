package com.example.shopingofmine.ui.search

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.shopingofmine.R
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.data.remote.ResultWrapper
import com.example.shopingofmine.databinding.FragmentSearchBinding
import com.example.shopingofmine.ui.rtl
import com.example.shopingofmine.ui.sharedviewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var searchRecyclerAdapter: SearchRecyclerAdapter
    private val args: SearchFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)
        initSetSearchView()
        initCollectFlows()
        binding.searchView.onActionViewExpanded()
        binding.searchView.setQuery(args.query, false)
        searchRecyclerAdapter = SearchRecyclerAdapter(::onItemClick)
        binding.recyclerView.adapter = searchRecyclerAdapter

    }

    private fun initCollectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchedResult.collectLatest {
                    when (it) {
                        ResultWrapper.Loading -> {}
                        is ResultWrapper.Success -> {
                            binding.loadingAnim.pauseAnimation()
                            binding.loadingAnim.isGone = true
                            searchRecyclerAdapter.submitList(it.value)

                        }
                        is ResultWrapper.Error -> {
                            val alertDialog: AlertDialog? = activity?.let {
                                AlertDialog.Builder(it)
                            }?.setMessage(it.message)
                                ?.setTitle("خطا")
                                ?.setPositiveButton("تلاش مجدد") { _, _ ->
                                    viewModel.searchProducts(binding.searchView.query.toString())
                                }
                                ?.setNegativeButton("انصراف") { _, _ ->
                                }?.create()
                            alertDialog?.show()
                        }
                    }
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
        findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToProductDetailsFragment(product.id))
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}