package com.example.shopingofmine.ui.orderdetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.shopingofmine.R
import com.example.shopingofmine.databinding.FragmentOrderDetailsBinding
import com.example.shopingofmine.model.localdataclass.LocalLineItem
import com.example.shopingofmine.model.localdataclass.LocalOrderClass
import com.example.shopingofmine.model.localdataclass.LocalOrderShipping
import com.example.shopingofmine.ui.sharedviewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailsFragment : Fragment(R.layout.fragment_order_details) {
    private var _binding: FragmentOrderDetailsBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: OrderDetailsViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOrderDetailsBinding.bind(view)
        val lineItems = mutableListOf<LocalLineItem>()
        sharedViewModel.cartItems.forEach {
            lineItems.add(LocalLineItem(it.key.id, it.value))
        }
        binding.orderButton.setOnClickListener {
            val shipping = LocalOrderShipping(
                first_name = binding.firstName.text.toString(),
                last_name = binding.lastName.text.toString(),
                city = binding.city.text.toString(),
                address_1 = binding.address.text.toString(),
                postcode = binding.postCode.text.toString()
            )
            viewModel.addOrder(LocalOrderClass(lineItems, shipping))
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}