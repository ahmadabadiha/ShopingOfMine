package com.example.shopingofmine.ui.cart

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
import com.example.shopingofmine.R
import com.example.shopingofmine.databinding.FragmentCartBinding
import com.example.shopingofmine.model.serverdataclass.ProductItem
import com.example.shopingofmine.ui.sharedviewmodel.SharedViewModel
import com.example.shopingofmine.util.ResultWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: CartViewModel by viewModels()
    private lateinit var cartRecyclerAdapter: CartRecyclerAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCartBinding.bind(view)
        if (savedInstanceState == null) {
            val cartProducts = sharedViewModel.cartItems.keys
            viewModel.productIds = cartProducts.map {
                it.id
            }.toTypedArray()
            viewModel.getCartProducts()
        }

        initSetRecyclerAdapter()
        initSetViews()
        initSetOnClickListeners()
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cartProducts.collectLatest {
                    when (it) {
                        ResultWrapper.Loading -> {
                            binding.loadingAnim.playAnimation()
                        }
                        is ResultWrapper.Success -> {
                            cartRecyclerAdapter.submitList(it.value)
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
                                    viewModel.getCartProducts()
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

    private fun initSetOnClickListeners() {
        binding.continueProcessButton.setOnClickListener {

        }
    }

    private fun initSetViews() {
        val discountComputed = viewModel.discountComputer(sharedViewModel.cartItems)
        val cartSumAmount = viewModel.priceComputerWithDiscount(sharedViewModel.cartItems).toString()

        binding.apply {
            (sharedViewModel.cartItems.values.sum().toString() + " کالا").also { productCount.text = it }
            (viewModel.priceComputerWithoutDiscount(sharedViewModel.cartItems).toString() + " ریال").also { productsPrice.text = it }
            (discountComputed.first.toString() + "ریال " + discountComputed.second.toString() + "%").also { discount.text = it }
            ("$cartSumAmount ریال").also { cartSum.text = it }
            ("$cartSumAmount ریال").also { bottomPrice.text = it }
        }
    }


    private fun initSetRecyclerAdapter() {
        val countList = sharedViewModel.cartItems.values.toList()
        cartRecyclerAdapter = CartRecyclerAdapter(countList, ::onItemClick)
        binding.recyclerView.adapter = cartRecyclerAdapter
    }

    private fun onItemClick(product: ProductItem) {
        sharedViewModel.productItem = product
        findNavController().navigate(CartFragmentDirections.actionCartFragmentToProductDetailsFragment())
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}