package com.example.shopingofmine.ui.productdetails

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.shopingofmine.R
import com.example.shopingofmine.databinding.FragmentProductDetailsBinding
import com.example.shopingofmine.data.model.serverdataclass.ProductItem
import com.example.shopingofmine.ui.sharedviewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var product: ProductItem
    private lateinit var viewPagerAdapter: ImageViewPagerAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductDetailsBinding.bind(view)

        product = sharedViewModel.productItem
        val imageUrls = product.images.map { image ->
            image.src
        }
        viewPagerAdapter = ImageViewPagerAdapter(imageUrls)

        initSetViews()
        initSetClickListeners()
        setUpViewPager()
    }

    private fun initSetClickListeners() {

        binding.addToCartButton.setOnClickListener {
            it.isGone = true
            binding.productCountLayout.isGone = false
            Log.d("ahmad", "initSetClickListeners: " + product.toString())
            sharedViewModel.cartItems[product] = 1
            Toast.makeText(requireContext(), "کالا به سبد خرید شما افزوده شد.", Toast.LENGTH_SHORT).show()
        }

        binding.add.setOnClickListener {
            val count = binding.count.text.toString().toInt() + 1
            binding.count.text = count.toString()
            sharedViewModel.cartItems[product] = count
            Log.d("ahmad", "initSetClickListeners: " + sharedViewModel.cartItems.size)
            Toast.makeText(requireContext(), "کالا به سبد خرید شما افزوده شد.", Toast.LENGTH_SHORT).show()
            if (count != 1) {
                val price = product.price.toInt() * count
                val priceString = "%,d".format(price) + " ریال"
                binding.bottomPrice.text = priceString
            }
        }

        binding.subtract.setOnClickListener {

            val count = binding.count.text.toString().toInt() - 1
            if (count == 0) {
                sharedViewModel.cartItems.remove(product)
                binding.productCountLayout.isGone = true
                binding.addToCartButton.isGone = false
                Toast.makeText(requireContext(), "کالا از سبد خرید شما حذف شد.", Toast.LENGTH_SHORT).show()
            } else {
                binding.count.text = count.toString()
                sharedViewModel.cartItems[product] = count
                Toast.makeText(requireContext(), "کالا از سبد خرید شما حذف شد.", Toast.LENGTH_SHORT).show()
            }
            if (count != 0) {
                val price = product.price.toInt() * count
                val priceString = "%,d".format(price) + " ریال"
                binding.bottomPrice.text = priceString
            }
        }
    }


    private fun initSetViews() {
        binding.productName.text = product.name
        val description =
            product.description.replace("<br />", "").replace("<p>", "").replace("</ br>", "").replace("</", "")
                .replace("<br", "").replace("p>", "").replace("<p", "")
        binding.description.text = description
        val price = "%,d".format(product.price.toInt()) + " ریال"
        if (product.price.isNotBlank()) binding.price.text = price
        binding.bottomPrice.text = price
    }

    private fun setUpViewPager() {
        binding.viewPager.adapter = viewPagerAdapter
        binding.indicator.setViewPager(binding.viewPager)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}