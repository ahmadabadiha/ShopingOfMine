package com.example.shopingofmine.ui.productdetails

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.shopingofmine.R
import com.example.shopingofmine.databinding.FragmentProductDetailsBinding
import com.example.shopingofmine.ui.sharedviewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var viewPagerAdapter: ImageViewPagerAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductDetailsBinding.bind(view)
        val product = sharedViewModel.productItem
        val imageUrls = product.images.map { image ->
            image.src
        }

        binding.productName.text = product.name
        val description =
            product.description.replace("<br />", "").replace("<p>", "").replace("</ br>", "").replace("</", "")
                .replace("<br", "").replace("p>", "").replace("<p", "")
        binding.description.text = description
        val price = "%,d".format(product.price.toInt()) + " ریال"
        if (product.price.isNotBlank()) binding.price.text = price
        binding.bottomPrice.text = price
        viewPagerAdapter = ImageViewPagerAdapter(imageUrls)
        Log.d("ahmad", "onViewCreated: " + imageUrls.toString())
        setUpViewPager()
        binding.addToCartButton.setOnClickListener {
            //todo
            findNavController().navigate(ProductDetailsFragmentDirections.actionProductDetailsFragmentToCartFragment())
        }
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