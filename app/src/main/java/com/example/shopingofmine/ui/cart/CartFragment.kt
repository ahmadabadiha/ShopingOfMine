package com.example.shopingofmine.ui.cart

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.shopingofmine.R
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.data.remote.ResultWrapper
import com.example.shopingofmine.databinding.FragmentCartBinding
import com.example.shopingofmine.notificationworkmanager.NotificationWorker
import com.example.shopingofmine.ui.buildAndShowErrorDialog
import com.example.shopingofmine.ui.collectFlow
import com.example.shopingofmine.ui.sharedviewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
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

        if (savedInstanceState == null) {
            viewModel.getCustomerOrder()
        }
        initSetOnClickListeners()
        initCollectFlows()

    }

    private fun initCollectFlows() {
        collectFlow(viewModel.cartProducts) {
            when (it) {
                ResultWrapper.Loading -> {
                    binding.loadingAnim.playAnimation()
                    binding.loadingAnim.isGone = false
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
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                }
                is ResultWrapper.Error -> {
                    binding.loadingAnim.isGone = true
                    binding.loadingAnim.pauseAnimation()
                    buildAndShowErrorDialog(message = it.message) { viewModel.getCustomerOrder() }
                }
            }
        }
        collectFlow(viewModel.errorInViewModelApiCalls) { errorMessage ->
            binding.loadingAnim.isGone = true
            binding.loadingAnim.pauseAnimation()
            binding.productsGroup.isGone = true
            if (errorMessage == "cart empty") {
                binding.emptyGroup.isGone = false
                binding.emptyAnim.playAnimation()
                WorkManager.getInstance(requireContext()).cancelUniqueWork("cart notification")
            } else {
                buildAndShowErrorDialog(message =  errorMessage) { viewModel.getCustomerOrder() }
            }
        }
        collectFlow(viewModel.coupon) {
            when (it) {
                ResultWrapper.Loading -> {
                    binding.loadingAnim.isGone = false
                    binding.loadingAnim.playAnimation()
                }
                is ResultWrapper.Success -> {
                    binding.loadingAnim.isGone = true
                    binding.loadingAnim.pauseAnimation()
                    val coupons = it.value
                    if (coupons.isEmpty()) Toast.makeText(requireContext(), "کد تخفیف نامعتبر است.", Toast.LENGTH_SHORT).show()
                    else {
                        Toast.makeText(requireContext(), "right", Toast.LENGTH_SHORT).show()
                    }
                }
                is ResultWrapper.Error -> {
                    buildAndShowErrorDialog(it.message,"خطا در بررسی کد تخفیف") { viewModel.getCoupon(binding.couponET.text.toString()) }
                    binding.loadingAnim.isGone = true
                    binding.loadingAnim.pauseAnimation()
                }
            }
        }
    }

    private fun initSetOnClickListeners() {
        binding.continueProcessButton.setOnClickListener {
            sharedViewModel.cartProducts = cartProducts
            sharedViewModel.order = viewModel.order
            findNavController().navigate(CartFragmentDirections.actionCartFragmentToOrderDetailsFragment())
        }

        binding.checkCoupon.setOnClickListener {
            val code = binding.couponET.text.toString()
            if (code.isNotBlank()) {
                viewModel.getCoupon(code)
            } else Toast.makeText(requireContext(), "کد تخفیف وارد نشده است.", Toast.LENGTH_SHORT).show()

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
        binding.loadingAnim.playAnimation()
        binding.loadingAnim.isGone = false
        viewModel.addToCart(product)
    }

    private fun onItemSubtractClick(product: ProductItem) {
        binding.loadingAnim.playAnimation()
        binding.loadingAnim.isGone = false
        viewModel.removeFromCart(product)
    }


    override fun onStop() {
        sharedViewModel.countList.clear()
        sharedViewModel.countList.addAll(viewModel.countList)
        enqueueWork()
        super.onStop()
    }

    override fun onDestroy() {
        binding.loadingAnim.isGone = false
        binding.emptyGroup.isGone = true
        binding.emptyAnim.pauseAnimation()
        _binding = null
        super.onDestroy()
    }

    private fun enqueueWork() {
        val countList = sharedViewModel.countList
        if (countList.isNotEmpty()) {
            Log.d("ahmadabadi", "onDestroy: work manager updated" + countList.toString() + countList.size.toString())
            val cartTotalCount = countList.sum()
            val interval = sharedViewModel.notificationTimeInterval
            val cartNotificationWorker =
                PeriodicWorkRequestBuilder<NotificationWorker>(interval!!.toLong(), TimeUnit.HOURS)
                    .setInputData(workDataOf("count" to cartTotalCount))
                    //  .setInitialDelay(10,TimeUnit.MINUTES)
                    .build()
            WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
                "cart notification",
                ExistingPeriodicWorkPolicy.REPLACE,
                cartNotificationWorker
            )
        }
    }
}