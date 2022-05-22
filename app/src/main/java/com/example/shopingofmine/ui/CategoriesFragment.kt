package com.example.shopingofmine.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.shopingofmine.R
import com.example.shopingofmine.databinding.FragmentCategoriesBinding
import com.example.shopingofmine.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment: Fragment(R.layout.fragment_categories) {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCategoriesBinding.bind(view)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}