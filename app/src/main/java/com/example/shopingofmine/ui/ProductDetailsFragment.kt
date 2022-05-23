package com.example.shopingofmine.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.shopingofmine.R
import com.example.shopingofmine.databinding.FragmentHomeBinding
import com.example.shopingofmine.databinding.FragmentProductDetailsBinding
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
        setUpViewPager()
    }

    private fun setUpViewPager() {

        binding.viewPager.adapter = viewPagerAdapter

        binding.viewPager.currentItem = 0

      /*  binding.viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    //update the image number textview
                    binding.imageNumberTV.text = "${position + 1} / 4"
                }
            }
        )*/
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}