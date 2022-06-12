package com.example.shopingofmine.ui.productdetails

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.shopingofmine.R
import com.example.shopingofmine.databinding.FragmentProductDetailsBinding
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.data.remote.ResultWrapper
import com.example.shopingofmine.ui.adapters.ShortReviewsRecyclerAdapter
import com.example.shopingofmine.ui.sharedviewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var product: ProductItem
    private lateinit var viewPagerAdapter: ImageViewPagerAdapter
    private lateinit var shortReviewsRecyclerAdapter: ShortReviewsRecyclerAdapter
    val viewModel: ProductDetailsViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductDetailsBinding.bind(view)

        product = sharedViewModel.productItem

        initSetViews()
        initSetClickListeners()
        setUpViewPager()
        initSetReviewsRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.customerIsKnown.collectLatest {
                    if (!it) {
                        val alertDialog: AlertDialog? = activity?.let {
                            AlertDialog.Builder(it)
                        }?.setMessage("برای اضافه کردن کالا به سبد خرید ابتدا باید وارد شوید.")
                            ?.setTitle("خطا")
                            ?.setPositiveButton("ثبت نام") { _, _ ->
                                findNavController().navigate(ProductDetailsFragmentDirections.actionProductDetailsFragmentToLoginFragment())
                            }
                            ?.setNegativeButton("انصراف") { _, _ ->
                            }?.create()
                        alertDialog?.show()
                    }
                }
            }
        }
    }

    private fun initSetReviewsRecyclerView() {
        shortReviewsRecyclerAdapter = ShortReviewsRecyclerAdapter(::onReviewItemClick)
        binding.recyclerView.adapter = shortReviewsRecyclerAdapter
        collectReviews()

    }

    private fun collectReviews() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getProductReviews(product.id).collectLatest {
                    when (it) {
                        ResultWrapper.Loading -> {
                            binding.reviewsCount.text = "در حال بارگیری"
                        }
                        is ResultWrapper.Success -> {
                            val reviews = it.value
                            (reviews.size.toString() + " دیدگاه").also { binding.reviewsCount.text = it }
                            shortReviewsRecyclerAdapter.submitList(reviews)
                        }
                        is ResultWrapper.Error -> {
                            val alertDialog: AlertDialog? = activity?.let {
                                AlertDialog.Builder(it)
                            }?.setMessage(it.message)
                                ?.setTitle(" خطا در بارگیری نظرات")
                                ?.setPositiveButton("تلاش مجدد") { _, _ ->
                                    collectReviews()
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

    private fun initSetClickListeners() {

        binding.addToCartButton.setOnClickListener {

            it.isGone = true
            binding.productCountLayout.isGone = false
            //sharedViewModel.addToCart(product)
            viewModel.addToCart(product)
            callAndCollectOrderResponses()
            Toast.makeText(requireContext(), "کالا به سبد خرید شما افزوده شد.", Toast.LENGTH_SHORT).show()
        }

        binding.add.setOnClickListener {
            val count = binding.count.text.toString().toInt() + 1
            binding.count.text = count.toString()
            viewModel.addToCart(product)
            //sharedViewModel.addToCart(product)
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

                //sharedViewModel.removeFromCart(product)
                binding.productCountLayout.isGone = true
                binding.addToCartButton.isGone = false
                Toast.makeText(requireContext(), "کالا از سبد خرید شما حذف شد.", Toast.LENGTH_SHORT).show()
            } else {
                binding.count.text = count.toString()
                viewModel.removeFromCart(product)
               // sharedViewModel.removeFromCart(product)
                Toast.makeText(requireContext(), "کالا از سبد خرید شما حذف شد.", Toast.LENGTH_SHORT).show()
                val price = product.price.toInt() * count
                val priceString = "%,d".format(price) + " ریال"
                binding.bottomPrice.text = priceString
            }
        }

        binding.addReviewButton.setOnClickListener {
            findNavController().navigate(ProductDetailsFragmentDirections.actionProductDetailsFragmentToAddReviewFragment())
        }
    }

    private fun callAndCollectOrderResponses() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                //viewModel.addOrder()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun initSetViews() {
        binding.productName.text = product.name
        val description =
            HtmlCompat.fromHtml(
                HtmlCompat.fromHtml(product.description, HtmlCompat.FROM_HTML_MODE_COMPACT).toString(),
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )
        binding.description.text = description
        val price = "%,d".format(product.price.toInt()) + " ریال"
        if (product.price.isNotBlank()) binding.price.text = price
        binding.bottomPrice.text = price
    }

    private fun setUpViewPager() {
        val imageUrls = product.images.map { image ->
            image.src
        }
        viewPagerAdapter = ImageViewPagerAdapter(imageUrls)
        binding.viewPager.adapter = viewPagerAdapter
        binding.indicator.setViewPager(binding.viewPager)
    }

    private fun onReviewItemClick() {
        findNavController().navigate(ProductDetailsFragmentDirections.actionProductDetailsFragmentToReviewsFragment(product.id))
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}