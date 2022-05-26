package com.example.shopingofmine.ui.products

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.shopingofmine.R
import com.example.shopingofmine.databinding.FragmentProductsBinding
import com.example.shopingofmine.model.serverdataclass.ProductItem
import com.example.shopingofmine.ui.adapters.ProductsRecyclerAdapter
import com.example.shopingofmine.ui.sharedviewmodel.SharedViewModel
import com.example.shopingofmine.util.ResultWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductsFragment : Fragment(R.layout.fragment_products) {
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private val navArgs: ProductsFragmentArgs by navArgs()
    private val viewModel: ProductsViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductsBinding.bind(view)
        val category = navArgs.categoryId
        viewModel.getProducts(category)
        val productsRecyclerAdapter = ProductsRecyclerAdapter(::onItemClick)
        binding.recyclerView.adapter = productsRecyclerAdapter
        viewModel.products.collectIt(viewLifecycleOwner){
            when (it) {
                ResultWrapper.Loading -> {
                    binding.loadingAnim.playAnimation()
                }
                is ResultWrapper.Success -> {
                    productsRecyclerAdapter.submitList(it.value)
                    binding.recyclerView.isGone = false
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                }
                is ResultWrapper.Error -> {
                    val alertDialog: AlertDialog? = activity?.let {
                        AlertDialog.Builder(it)
                    }?.setMessage(it.message)
                        ?.setTitle("خطا")
                        ?.setPositiveButton("تلاش مجدد") { _, _ ->
                            viewModel.getProducts(category)
                        }
                        ?.setNegativeButton("انصراف") { _, _ ->
                        }?.create()
                    alertDialog?.show()
                }
            }
        }
    }

    private fun onItemClick(product: ProductItem) {
        sharedViewModel.productItem = product
        findNavController().navigate(ProductsFragmentDirections.actionProductsFragmentToProductDetailsFragment())
    }

    fun <T> StateFlow<T>.collectIt(lifecycleOwner: LifecycleOwner, function: (T) -> Unit) {
        lifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                collect {
                    function.invoke(it)
                }
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}