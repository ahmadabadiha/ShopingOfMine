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
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.shopingofmine.R
import com.example.shopingofmine.data.NotificationWorker
import com.example.shopingofmine.databinding.FragmentCartBinding
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.ui.sharedviewmodel.SharedViewModel
import com.example.shopingofmine.data.remote.ResultWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: CartViewModel by viewModels()
    private lateinit var cartProducts: List<ProductItem>
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

            viewModel.getCustomerOrder()
            Log.d("http", "onViewCreated: ")
        }

        initSetOnClickListeners()
        initCollectFlows()

    }

    private fun initCollectFlows() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cartProducts.collectLatest {
                    launch {
                        when (it) {
                            ResultWrapper.Loading -> {
                                binding.loadingAnim.playAnimation()
                            }
                            is ResultWrapper.Success -> {
                                val countList = viewModel.countList
                                cartRecyclerAdapter =
                                    CartRecyclerAdapter(countList, ::onItemImageClick, ::onItemAddClick, ::onItemSubtractClick)
                                binding.recyclerView.adapter = cartRecyclerAdapter
                                cartProducts = it.value
                                cartRecyclerAdapter.submitList(cartProducts)
                                setViews(countList)
                                Log.d("ahmadabadi", "initCollectFlows: " + countList.toString())
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
                    launch {
                        viewModel.errorInViewModelApiCalls.collectLatest {

                        }
                    }
                }
            }

        }
    }

    private fun initSetOnClickListeners() {
        binding.continueProcessButton.setOnClickListener {
            sharedViewModel.countList = viewModel.countList
            sharedViewModel.cartProducts = cartProducts
            sharedViewModel.order = viewModel.order
            findNavController().navigate(CartFragmentDirections.actionCartFragmentToOrderDetailsFragment())
        }
    }

    private fun setViews(productsCount: MutableList<Int>) {
        val discountComputed = viewModel.computeDiscount(cartProducts, productsCount)
        val cartSumAmount = viewModel.computePriceWithDiscount(cartProducts, productsCount)
        val priceWithoutDiscountComputed = viewModel.computePriceWithoutDiscount(cartProducts, productsCount)
        binding.apply {
            (productsCount.sum().toString() + " کالا").also { productCount.text = it }
            ("%,d".format(priceWithoutDiscountComputed) + " ریال").also { productsPrice.text = it }
            ("(${discountComputed.second}%) " + "%,d".format(discountComputed.first) + " ریال").also { discount.text = it }
            ("%,d".format(cartSumAmount) + " ریال").also { cartSum.text = it }
            ("%,d".format(cartSumAmount) + " ریال").also { bottomPrice.text = it }
        }
    }

    private fun onItemImageClick(product: ProductItem) {
        sharedViewModel.productItem = product
        findNavController().navigate(CartFragmentDirections.actionCartFragmentToProductDetailsFragment(product.id))
    }

    private fun onItemAddClick(product: ProductItem) {

        val countList = viewModel.countList
        cartRecyclerAdapter.countList = countList
        viewModel.addToCart(product)

    }

    private fun onItemSubtractClick(product: ProductItem) {

        val countList = viewModel.countList
        cartRecyclerAdapter.countList = countList
        viewModel.removeFromCart(product)

    }

    override fun onDestroy() {
        binding.loadingAnim.isGone = false
        binding.emptyGroup.isGone = true
        binding.emptyAnim.pauseAnimation()
        _binding = null
        if (requireActivity().isFinishing) {
            val countList = viewModel.countList
            if (countList.isNotEmpty()) {
                val cartTotalCount = countList.sum()
                val cartNotificationWorker =
                    PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES)
                        .setInputData(workDataOf("count" to cartTotalCount))
                        .build()
                WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
                    "sendLogs",
                    ExistingPeriodicWorkPolicy.REPLACE,
                    cartNotificationWorker
                )
            }
        }
        super.onDestroy()
    }
}