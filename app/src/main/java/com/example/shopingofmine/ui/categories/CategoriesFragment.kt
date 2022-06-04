package com.example.shopingofmine.ui.categories

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.shopingofmine.R
import com.example.shopingofmine.data.remote.ResultWrapper
import com.example.shopingofmine.databinding.FragmentCategoriesBinding
import com.example.shopingofmine.ui.adapters.CategoriesRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoriesFragment : Fragment(R.layout.fragment_categories) {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoriesViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCategoriesBinding.bind(view)
        val categoriesRecyclerAdapter = CategoriesRecyclerAdapter(::onItemClick)
        binding.recyclerView.adapter = categoriesRecyclerAdapter
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.categories.collectLatest {
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
                            is ResultWrapper.Error -> {
                                val alertDialog: AlertDialog? = activity?.let {
                                    AlertDialog.Builder(it)
                                }?.setMessage(it.message)
                                    ?.setTitle("خطا")
                                    ?.setPositiveButton("تلاش مجدد") { _, _ ->
                                        viewModel.getCategories()
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
    }

    private fun onItemClick(categoryId: String) {
        findNavController().navigate(CategoriesFragmentDirections.actionCategoriesFragmentToProductsFragment(categoryId, null))
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}