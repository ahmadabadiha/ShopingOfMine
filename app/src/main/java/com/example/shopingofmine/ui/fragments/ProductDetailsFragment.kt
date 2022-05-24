package com.example.shopingofmine.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.shopingofmine.R
import com.example.shopingofmine.databinding.FragmentProductDetailsBinding
import com.example.shopingofmine.ui.ImageViewPagerAdapter
import com.example.shopingofmine.ui.viewmodels.SharedViewModel
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
       viewPagerAdapter = ImageViewPagerAdapter(imageUrls)
        Log.d("ahmad", "onViewCreated: " + imageUrls.toString())
        setUpViewPager()
    }

    private fun setUpViewPager() {

        binding.viewPager.adapter = viewPagerAdapter
        binding.indicator.setViewPager(binding.viewPager)

        //binding.viewPager.currentItem = 0

      /*  binding.viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    //update the image number textview
                    binding.imageNumberTV.text = "${position + 1} / 4"
                }
            }
        )*/
        binding.productName.text = sharedViewModel.productItem.name
        binding.description.text = sharedViewModel.productItem.description
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}