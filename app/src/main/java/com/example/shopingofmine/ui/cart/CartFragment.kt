package com.example.shopingofmine.ui.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.shopingofmine.R
import com.example.shopingofmine.databinding.FragmentCartBinding
import com.example.shopingofmine.databinding.FragmentCategoriesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCartBinding.bind(view)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}