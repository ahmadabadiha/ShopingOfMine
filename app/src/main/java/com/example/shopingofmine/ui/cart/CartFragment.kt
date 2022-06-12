package com.example.shopingofmine.ui.cart

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
import com.example.shopingofmine.databinding.FragmentCartBinding
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.ui.sharedviewmodel.SharedViewModel
import com.example.shopingofmine.data.remote.ResultWrapper
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
      //  if (sharedViewModel.cartItems.isEmpty()) {
        //    binding.loadingAnim.isGone = true
        //    binding.emptyGroup.isGone = false
        //    binding.emptyAnim.playAnimation()
       // } else {
            if (savedInstanceState == null) {
                val cartProducts = sharedViewModel.cartItems.keys

                viewModel.getCustomerOrder()
                Log.d("http", "onViewCreated: ")
            }
            //initSetRecyclerAdapter()
            initSetViews()
            initSetOnClickListeners()
            initCollectFlows()
       // }
    }

    private fun initCollectFlows() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cartProducts.collectLatest {
                    when (it) {
                        ResultWrapper.Loading -> {
                            binding.loadingAnim.playAnimation()
                        }
                        is ResultWrapper.Success -> {
                            val countList = viewModel.countList
                            cartRecyclerAdapter = CartRecyclerAdapter(countList, ::onItemImageClick, ::onItemAddClick, ::onItemSubtractClick)
                            binding.recyclerView.adapter = cartRecyclerAdapter
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
                                    viewModel.getCustomerOrder()
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
            findNavController().navigate(CartFragmentDirections.actionCartFragmentToOrderDetailsFragment())
        }
    }

    private fun initSetViews() {
        val discountComputed = viewModel.computeDiscount(sharedViewModel.cartItems)
        val cartSumAmount = viewModel.computePriceWithDiscount(sharedViewModel.cartItems)

        binding.apply {
            (sharedViewModel.cartItems.values.sum().toString() + " کالا").also { productCount.text = it }
            (viewModel.computePriceWithoutDiscount(sharedViewModel.cartItems).toString() + " ریال").also { productsPrice.text = it }
            ("(${discountComputed.second}%) " + "%,d".format(discountComputed.first) + " ریال").also { discount.text = it }
            ("%,d".format(cartSumAmount) + " ریال").also { cartSum.text = it }
            ("%,d".format(cartSumAmount) + " ریال").also { bottomPrice.text = it }
        }
    }


    private fun initSetRecyclerAdapter() {
        val countList = viewModel.countList
        cartRecyclerAdapter = CartRecyclerAdapter(countList, ::onItemImageClick, ::onItemAddClick, ::onItemSubtractClick)
        binding.recyclerView.adapter = cartRecyclerAdapter
    }

    private fun onItemImageClick(product: ProductItem) {
        sharedViewModel.productItem = product
        findNavController().navigate(CartFragmentDirections.actionCartFragmentToProductDetailsFragment())
    }

    private fun onItemAddClick(product: ProductItem) {
        val productCount = sharedViewModel.cartItems[product]?.plus(1)
        if (productCount != null) {
           // sharedViewModel.addToCart(product)
        }
        val countList = sharedViewModel.cartItems.values.toList()
        cartRecyclerAdapter.countList = countList
        initSetViews()
    }

    private fun onItemSubtractClick(product: ProductItem) {

        val productCount = sharedViewModel.cartItems[product]?.minus(1)
        if (productCount != 0) {
            if (productCount != null) {
                //sharedViewModel.removeFromCart(product)
            }
        } //else sharedViewModel.removeFromCart(product)
        val countList = sharedViewModel.cartItems.values.toList()
        cartRecyclerAdapter.submitList(sharedViewModel.cartItems.keys.toList())
        cartRecyclerAdapter.countList = countList
        initSetViews()
    }

    override fun onDestroy() {
        binding.loadingAnim.isGone = false
        binding.emptyGroup.isGone = true
        binding.emptyAnim.pauseAnimation()
        _binding = null
        super.onDestroy()
    }
}