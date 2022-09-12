package com.example.shopingofmine.ui.completeorder

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shopingofmine.R
import com.example.shopingofmine.ShoppingNavDirections
import com.example.shopingofmine.data.model.apimodels.Order
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.data.remote.ResultWrapper
import com.example.shopingofmine.databinding.FragmentCompleteOrderBinding
import com.example.shopingofmine.ui.buildAndShowErrorDialog
import com.example.shopingofmine.ui.collectFlow
import com.example.shopingofmine.ui.sharedviewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CompleteOrderFragment : Fragment(R.layout.fragment_complete_order) {
    private var _binding: FragmentCompleteOrderBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: CompleteOrderViewModel by viewModels()
    private lateinit var cartProducts: List<ProductItem>
    private lateinit var countList: List<Int>
    private lateinit var order: Order
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCompleteOrderBinding.bind(view)
        cartProducts = sharedViewModel.cartProducts
        countList = sharedViewModel.countList
        order = sharedViewModel.order

        setViews()

        binding.recyclerView.adapter = CompleteOrderProductsRecyclerAdapter(::onItemClick, countList).also {
            it.submitList(cartProducts)
        }

        binding.completeOrderButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                updateOrderAndCollectResult()
            }
        }
    }

    private suspend fun updateOrderAndCollectResult() {
        collectFlow(viewModel.updateOrder(order.id)) {
            when (it) {
                ResultWrapper.Loading -> {
                    binding.loadingAnim.playAnimation()
                    binding.loadingAnim.isGone = false
                }
                is ResultWrapper.Success -> {
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                    Toast.makeText(requireContext(), "سفارش شما با موفقیت ثبت شد.", Toast.LENGTH_LONG).show()
                    findNavController().navigate(CompleteOrderFragmentDirections.actionCompleteOrderFragmentToHomeFragment())
                }
                is ResultWrapper.Error -> {
                    buildAndShowErrorDialog(it.message) {
                        viewLifecycleOwner.lifecycleScope.launch { updateOrderAndCollectResult() }
                    }
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                }
            }
        }
    }

    private fun setViews() {
        val discountComputed = viewModel.computeDiscount(cartProducts, countList)
        val cartSumAmount = viewModel.computePriceWithDiscount(cartProducts, countList)
        val priceWithoutDiscountComputed = viewModel.computePriceWithoutDiscount(cartProducts, countList)
        val shipping = order.shipping
        val address = shipping.city + "\n" + shipping.address_1 + "\n" + shipping.first_name + "\n" + shipping.last_name
        binding.apply {
            binding.address.text = address
            (countList.sum().toString() + " کالا").also { productCount.text = it }
            ("%,d".format(priceWithoutDiscountComputed) + " ریال").also { productsPrice.text = it }
            ("(${discountComputed.second}%) " + "%,d".format(discountComputed.first) + " ریال").also { discount.text = it }
            ("%,d".format(cartSumAmount) + " ریال").also { cartSum.text = it }
        }
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