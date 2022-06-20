package com.example.shopingofmine.ui.cart

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.data.remote.ResultWrapper
import com.example.shopingofmine.databinding.FragmentCartBinding
import com.example.shopingofmine.ui.notificationworkmanager.NotificationWorker
import com.example.shopingofmine.ui.sharedviewmodel.SharedViewModel
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

        if (savedInstanceState == null) {
            viewModel.getCustomerOrder()
        }
        initSetOnClickListeners()
        initCollectFlows()

    }

    private fun initCollectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.cartProducts.collectLatest {
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
                }
                launch {
                    viewModel.errorInViewModelApiCalls.collectLatest { errorMessage ->
                        if (errorMessage == "cart empty") {
                            binding.loadingAnim.isGone = true
                            binding.emptyGroup.isGone = false
                            binding.productsGroup.isGone = true
                            binding.emptyAnim.playAnimation()
                            WorkManager.getInstance(requireContext()).cancelUniqueWork("cart notification")
                        } else {
                            val alertDialog: AlertDialog? = activity?.let {
                                AlertDialog.Builder(it)
                            }?.setMessage(errorMessage)
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
                    viewModel.coupon.collectLatest {
                        when (it) {
                            ResultWrapper.Loading -> {
                                //todo loading animation
                            }
                            is ResultWrapper.Success -> {
                                val coupons = it.value
                                if (coupons.isEmpty()) Toast.makeText(requireContext(), "کد تخفیف نامعتبر است.", Toast.LENGTH_SHORT).show()
                                else {
                                    Toast.makeText(requireContext(), "right", Toast.LENGTH_SHORT).show()
                                }
                            }
                            is ResultWrapper.Error -> {
                                val alertDialog: AlertDialog? = activity?.let {
                                    AlertDialog.Builder(it)
                                }?.setMessage(it.message)
                                    ?.setTitle("خطا در بررسی کد تخفیف")
                                    ?.setPositiveButton("تلاش مجدد") { _, _ ->
                                        viewModel.getCoupon(binding.couponET.text.toString())
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
        viewModel.addToCart(product)
    }

    private fun onItemSubtractClick(product: ProductItem) {
        viewModel.removeFromCart(product)
    }


    override fun onStop() {
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