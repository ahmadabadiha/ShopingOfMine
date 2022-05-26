package com.example.shopingofmine.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.shopingofmine.R
import com.example.shopingofmine.databinding.FragmentCategoriesBinding
import com.example.shopingofmine.databinding.FragmentOptionsBinding
import com.example.shopingofmine.datastore.Theme
import com.example.shopingofmine.ui.viewmodels.OptionsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OptionsFragment : Fragment(R.layout.fragment_options) {
    private var _binding: FragmentOptionsBinding? = null
    private val binding get() = _binding!!
    private val optionsViewModel: OptionsViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOptionsBinding.bind(view)
        val isDarkMode = arguments?.getBoolean("darkMode") ?: false
        binding.themeSwitch.isChecked = isDarkMode
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) optionsViewModel.updateTheme(Theme.NIGHT)
                    else optionsViewModel.updateTheme(Theme.LIGHT)
                }
            }
        }

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}