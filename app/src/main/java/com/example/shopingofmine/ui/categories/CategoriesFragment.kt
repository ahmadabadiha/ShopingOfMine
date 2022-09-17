package com.example.shopingofmine.ui.categories

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shopingofmine.R
import com.example.shopingofmine.data.model.apimodels.CategoryItem
import com.example.shopingofmine.data.remote.ResultWrapper
import com.example.shopingofmine.databinding.FragmentCategoriesBinding
import com.example.shopingofmine.ui.buildAndShowErrorDialog
import com.example.shopingofmine.ui.collectFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : Fragment(R.layout.fragment_categories) {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoriesViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCategoriesBinding.bind(view)

        collectFlow(viewModel.categories) {
            when (it) {
                ResultWrapper.Loading -> {
                    binding.loadingAnim.playAnimation()
                    binding.loadingAnim.isGone = false
                }
                is ResultWrapper.Success -> {
                    val categories = it.value
                    val groupedCategories = groupCategories(categories)
                    val categoriesRecyclerAdapter =
                        CategoriesRecyclerAdapter(::onItemClick, groupedCategories)
                    binding.recyclerView.adapter = categoriesRecyclerAdapter
                    categoriesRecyclerAdapter.submitList(groupedCategories[0])
                    binding.recyclerView.isGone = false
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                }
                is ResultWrapper.Error -> {
                    buildAndShowErrorDialog(it.message) { viewModel.getCategories() }
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                }
            }
        }
    }

    private fun groupCategories(categories: List<CategoryItem>): Map<Int, List<CategoryItem>> {
        val groupedCategories = categories.groupBy { category ->
            category.parent
        }.toMutableMap()

        groupedCategories[0]!!.forEach {
            Log.d("ahmad", "groupCategories: before if ${it.id}")
            if (!groupedCategories.containsKey(it.id)) {
                Log.d("ahmad", "groupCategories: ${it.id}")
                groupedCategories[it.id] = listOf(it)
            }
        }
        return groupedCategories.toMap()
    }

    private fun onItemClick(categoryId: String) {
        findNavController().navigate(CategoriesFragmentDirections.actionCategoriesFragmentToProductsFragment(categoryId, null))
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}