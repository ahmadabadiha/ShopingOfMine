package com.example.shopingofmine.ui.reviews

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.shopingofmine.R
import com.example.shopingofmine.data.remote.ResultWrapper
import com.example.shopingofmine.databinding.FragmentReviewsBinding
import com.example.shopingofmine.ui.adapters.CompleteReviewsRecyclerAdapter
import com.example.shopingofmine.ui.buildAndShowErrorDialog
import com.example.shopingofmine.ui.collectFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviewsFragment : Fragment(R.layout.fragment_reviews) {
    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!
    val viewModel: ReviewsViewModel by viewModels()
    private lateinit var completeReviewsRecyclerAdapter: CompleteReviewsRecyclerAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentReviewsBinding.bind(view)
        viewLifecycleOwner.lifecycleScope.launch { collectReviewsFlow() }
    }

    private suspend fun collectReviewsFlow() {
        collectFlow(viewModel.getProductReviews()) {
            when (it) {
                ResultWrapper.Loading -> {
                    binding.shimmer.startShimmer()
                }
                is ResultWrapper.Success -> {
                    binding.shimmer.isGone = true
                    binding.shimmer.stopShimmer()
                    binding.recyclerView.isGone = false
                    completeReviewsRecyclerAdapter = CompleteReviewsRecyclerAdapter()
                    binding.recyclerView.adapter = completeReviewsRecyclerAdapter
                    completeReviewsRecyclerAdapter.submitList(it.value)
                }
                is ResultWrapper.Error -> {
                    buildAndShowErrorDialog(message = it.message, title = " خطا در بارگیری نظرات") {
                        viewLifecycleOwner.lifecycleScope.launch { collectReviewsFlow() }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}