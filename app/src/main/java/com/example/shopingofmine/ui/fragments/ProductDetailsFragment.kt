package com.example.shopingofmine.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.shopingofmine.R
import com.example.shopingofmine.databinding.FragmentProductDetailsBinding
import com.example.shopingofmine.ui.adapters.ImageViewPagerAdapter
import com.example.shopingofmine.ui.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.String


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
        binding.price.text = "%,d".format(product.price.toInt()) + " ریال"
        viewPagerAdapter = ImageViewPagerAdapter(imageUrls)
        Log.d("ahmad", "onViewCreated: " + imageUrls.toString())
        setUpViewPager()
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