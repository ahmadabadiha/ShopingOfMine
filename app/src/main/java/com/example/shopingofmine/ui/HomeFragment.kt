package com.example.shopingofmine.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.shopingofmine.R
import com.example.shopingofmine.databinding.FragmentHomeBinding
import com.example.shopingofmine.util.ResultWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    val TAG = "ahmad"
    private val viewModel: HomeViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        val popularRecyclerAdapter = ProductsRecyclerAdapter()
        val topRatedRecyclerAdapter = ProductsRecyclerAdapter()
        val recentRecyclerAdapter = ProductsRecyclerAdapter()
        binding.popularRecyclerView.adapter = popularRecyclerAdapter
        binding.topRatedRecyclerView.adapter = topRatedRecyclerAdapter
        binding.recentRecyclerView.adapter = recentRecyclerAdapter
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.popularProducts.collect {
                        when (it) {
                            ResultWrapper.Loading -> {
                            }//Toast.makeText(requireContext(), "Please wait", Toast.LENGTH_SHORT).show()
                            is ResultWrapper.Success -> popularRecyclerAdapter.submitList(it.value)
                            is ResultWrapper.Error -> Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                launch {
                    viewModel.topRatedProducts.collect {
                        when (it) {
                            ResultWrapper.Loading -> {
                            }//Toast.makeText(requireContext(), "Please wait", Toast.LENGTH_SHORT).show()
                            is ResultWrapper.Success -> topRatedRecyclerAdapter.submitList(it.value)
                            is ResultWrapper.Error -> Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                launch {
                    viewModel.newProducts.collect {
                        when (it) {
                            ResultWrapper.Loading -> Toast.makeText(requireContext(), "Please wait", Toast.LENGTH_SHORT).show()
                            is ResultWrapper.Success -> recentRecyclerAdapter.submitList(it.value)
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