package com.example.shopingofmine.ui.reviews

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.shopingofmine.R
import com.example.shopingofmine.data.remote.ResultWrapper
import com.example.shopingofmine.databinding.FragmentReviewsBinding
import com.example.shopingofmine.ui.adapters.CompleteReviewsRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
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
        collectReviewsFlow()

    }

    private fun collectReviewsFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.getProductReviews().collectLatest {
                    when (it) {
                        ResultWrapper.Loading -> {

                        }
                        is ResultWrapper.Success -> {
                            completeReviewsRecyclerAdapter = CompleteReviewsRecyclerAdapter()
                            binding.recyclerView.adapter = completeReviewsRecyclerAdapter
                            completeReviewsRecyclerAdapter.submitList(it.value)
                        }
                        is ResultWrapper.Error -> {
                            val alertDialog: AlertDialog? = activity?.let {
                                AlertDialog.Builder(it)
                            }?.setMessage(it.message)
                                ?.setTitle(" خطا در بارگیری نظرات")
                                ?.setPositiveButton("تلاش مجدد") { _, _ ->
                                    collectReviewsFlow()
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

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}