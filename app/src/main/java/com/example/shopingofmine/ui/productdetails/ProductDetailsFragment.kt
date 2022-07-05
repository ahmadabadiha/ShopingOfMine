package com.example.shopingofmine.ui.productdetails

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shopingofmine.R
import com.example.shopingofmine.data.model.apimodels.OrderLineItem
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.data.remote.ResultWrapper
import com.example.shopingofmine.databinding.FragmentProductDetailsBinding
import com.example.shopingofmine.ui.adapters.ShortReviewsRecyclerAdapter
import com.example.shopingofmine.ui.buildAndShowErrorDialog
import com.example.shopingofmine.ui.collectFlow
import com.example.shopingofmine.ui.sharedviewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint


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
        initSetOnClickListeners()
        setUpViewPager()
        initSetReviewsRecyclerView()
        initCollectFlows()

    }

    private fun initSetReviewsRecyclerView() {
        shortReviewsRecyclerAdapter = ShortReviewsRecyclerAdapter(::onReviewItemClick)
        binding.recyclerView.adapter = shortReviewsRecyclerAdapter
    }

    private fun initCollectFlows() {
        collectFlow(viewModel.review) {
            when (it) {
                ResultWrapper.Loading -> {
                    binding.reviewsCount.text = "در حال بارگیری"
                    binding.shimmer.startShimmer()
                    binding.shimmer.isGone = false
                }
                is ResultWrapper.Success -> {
                    binding.reviewGroup.isGone = false
                    binding.shimmer.stopShimmer()
                    binding.shimmer.isGone = true
                    val reviews = it.value
                    (reviews.size.toString() + " دیدگاه").also { binding.reviewsCount.text = it }
                    shortReviewsRecyclerAdapter.submitList(reviews)
                }
                is ResultWrapper.Error -> {
                    buildAndShowErrorDialog(it.message, " خطا در بارگیری نظرات") { viewModel.getProductReviews() }
                }
            }
        }
        collectFlow(viewModel.customerIsKnown) {
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
        collectFlow(viewModel.errorMessage) { errorMessage ->
            showCustomErrorDialog(errorMessage)
        }
    }

    private fun initSetOnClickListeners() {

        binding.addToCartButton.setOnClickListener {
            binding.loadingAnim.playAnimation()
            binding.loadingAnim.isGone = false
            it.isGone = true
            binding.productCountLayout.isGone = false
            viewModel.addToCart(product)
            collectOrderResponses()
        }

        binding.add.setOnClickListener {
            binding.loadingAnim.playAnimation()
            binding.loadingAnim.isGone = false
            viewModel.addToCart(product)
        }

        binding.subtract.setOnClickListener {
            binding.loadingAnim.playAnimation()
            binding.loadingAnim.isGone = false
            viewModel.removeFromCart(product)
        }

        binding.addReviewButton.setOnClickListener {
            findNavController().navigate(ProductDetailsFragmentDirections.actionProductDetailsFragmentToAddReviewFragment())
        }
    }

    private fun collectOrderResponses() {
        collectFlow(viewModel.updatedOrder) {
            when (it) {
                ResultWrapper.Loading -> {}
                is ResultWrapper.Success -> {
                    val order = it.value
                    for (item in order.line_items) {
                        if (item.product_id == product.id) {
                            updateViews(item)
                            Toast.makeText(requireContext(), "تغییرات در سبد خرید شما اعمال شد.", Toast.LENGTH_SHORT).show()
                            break
                        }
                    }
                }
                is ResultWrapper.Error -> {
                    showCustomErrorDialog(it.message)
                }
            }
        }
    }

    private fun showCustomErrorDialog(message: String?) {
        val alertDialog: AlertDialog? = activity?.let {
            AlertDialog.Builder(it,R.style.AlertDialogCustom)
        }?.setMessage("$message لطفا چند لحظه دیگر دوباره امتحان کنید. ")
            ?.setTitle(" خطا در بروزرسانی سفارش")
            ?.setNegativeButton("باشه") { _, _ ->
            }?.create()
        alertDialog?.show()
    }

    private fun updateViews(item: OrderLineItem) {
        val count = item.quantity
        binding.count.text = count.toString()
        val price = item.total.toInt() * count
        ("%,d".format(price) + " ریال").also { binding.bottomPrice.text = it }
        if (count == 0) {
            binding.productCountLayout.isGone = true
            binding.addToCartButton.isGone = false
        }
        binding.loadingAnim.pauseAnimation()
        binding.loadingAnim.isGone = true
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
