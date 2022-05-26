package com.example.shopingofmine.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.shopingofmine.R
import com.example.shopingofmine.databinding.FragmentCategoriesBinding
import com.example.shopingofmine.ui.adapters.CategoriesRecyclerAdapter
import com.example.shopingofmine.ui.viewmodels.CategoriesViewModel
import com.example.shopingofmine.util.ResultWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoriesFragment : Fragment(R.layout.fragment_categories) {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoriesViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCategoriesBinding.bind(view)
        val categoriesRecyclerAdapter = CategoriesRecyclerAdapter()
        binding.recyclerView.adapter = categoriesRecyclerAdapter
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.categories.collect {
                        when (it) {
                            ResultWrapper.Loading -> {
                                binding.loadingAnim.playAnimation()
                            }
                            is ResultWrapper.Success -> {
                                categoriesRecyclerAdapter.submitList(it.value)
                                binding.recyclerView.isGone = false
                                binding.loadingAnim.pauseAnimation()
                                binding.loadingAnim.isGone = true
                            }
                            is ResultWrapper.Error -> Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
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