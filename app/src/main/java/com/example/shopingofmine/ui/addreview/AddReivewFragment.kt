package com.example.shopingofmine.ui.addreview

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.shopingofmine.R
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.data.model.appmodels.AppReview
import com.example.shopingofmine.data.remote.ResultWrapper
import com.example.shopingofmine.databinding.FragmentAddReviewBinding
import com.example.shopingofmine.ui.buildAndShowErrorDialog
import com.example.shopingofmine.ui.collectFlow
import com.example.shopingofmine.ui.sharedviewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddReviewFragment : Fragment(R.layout.fragment_add_review) {
    private var _binding: FragmentAddReviewBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: AddReviewViewModel by viewModels()
    private lateinit var product: ProductItem
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddReviewBinding.bind(view)
        product = sharedViewModel.productItem
        initSetViews()
        initSetListeners()

    }

    private fun initSetListeners() {
        binding.slider.addOnChangeListener { _, value, _ ->
            viewModel.rating = value.toInt()
        }

        binding.addReviewButton.setOnClickListener {
            if (validateInputs()) {
                val review = AppReview(
                    product.id,
                    viewModel.rating,
                    binding.reviewText.text.toString(),
                    binding.reviewerName.text.toString(),
                    binding.email.text.toString()
                )
                viewLifecycleOwner.lifecycleScope.launch {
                    addReview(review)
                }
            } else Toast.makeText(requireContext(), "خطا: ورودی ها نامعتبر هستند.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initSetViews() {
        Glide.with(this).load(product.images[0].src).error(R.drawable.ic_baseline_error_outline_24)
            .transition(DrawableTransitionOptions.withCrossFade()).into(binding.productImage)
        binding.productName.text = product.name
        binding.slider.value = viewModel.rating.toFloat()
    }

    private fun validateInputs(): Boolean {
        return binding.reviewText.text.toString().isNotBlank()
                && binding.reviewerName.text.toString().isNotBlank()
                && binding.email.text.toString().isNotBlank()
                && isValidEmail(binding.email.text.toString())
    }

    private suspend fun addReview(review: AppReview) {
        collectFlow(viewModel.addReview(review)) {
            when (it) {
                ResultWrapper.Loading -> {
                    binding.loadingAnim.playAnimation()
                    binding.loadingAnim.isGone = false
                }
                is ResultWrapper.Success<*> -> {
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                    Toast.makeText(
                        requireContext(),
                        "با تشکر. نظر شما با موفقیت ثبت شد.",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(AddReviewFragmentDirections.actionAddReviewFragmentToProductDetailsFragment(product.id))
                }
                is ResultWrapper.Error<*> -> {
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                    buildAndShowErrorDialog(message = it.message, title = " خطا در ارسال دیدگاه"){
                        viewLifecycleOwner.lifecycleScope.launch { addReview(review) }
                    }
                }
            }
        }
    }

    private fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}