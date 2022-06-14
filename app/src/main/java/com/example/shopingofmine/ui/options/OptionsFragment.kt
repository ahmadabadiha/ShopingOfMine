package com.example.shopingofmine.ui.options

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.shopingofmine.R
import com.example.shopingofmine.databinding.FragmentOptionsBinding
import com.example.shopingofmine.data.datastore.Theme
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

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            updateTheme(isChecked)
        }

        setDropDownItems()
    }

    private fun updateTheme(isChecked: Boolean) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (isChecked) optionsViewModel.updateTheme(Theme.NIGHT)
                else optionsViewModel.updateTheme(Theme.LIGHT)
            }
        }
    }

    private fun setDropDownItems() {
        val items = listOf("3", "5", "8", "12")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        binding.autoCompleteTextView.setAdapter(adapter)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}