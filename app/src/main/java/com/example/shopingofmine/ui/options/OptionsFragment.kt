package com.example.shopingofmine.ui.options

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.shopingofmine.R
import com.example.shopingofmine.data.datastore.Theme
import com.example.shopingofmine.databinding.FragmentOptionsBinding
import com.example.shopingofmine.notificationworkmanager.NotificationWorker
import com.example.shopingofmine.ui.sharedviewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class OptionsFragment : Fragment(R.layout.fragment_options) {
    private var _binding: FragmentOptionsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OptionsViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    var oldTimeInterval = 0
    var newTimeInterval = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOptionsBinding.bind(view)
        oldTimeInterval = sharedViewModel.notificationTimeInterval!!
        newTimeInterval = oldTimeInterval
        setViews()
        setListeners()
        setDropDownItems()

    }

    private fun setListeners() {
        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            updateTheme(isChecked)
        }

        binding.timeIntervalET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(editable: Editable) {
                sharedViewModel.notificationTimeInterval = editable.toString().toInt()
                newTimeInterval = editable.toString().toInt()
            }

        })

        binding.customTimeET.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(editable: Editable) {
                if (editable.toString().isNotBlank()) {
                    sharedViewModel.notificationTimeInterval = editable.toString().toInt()
                    newTimeInterval = editable.toString().toInt()
                }
            }
        })

        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.timeIntervalET.isEnabled = false
                binding.timeInterval.isEnabled = false
                binding.customTimeET.isEnabled = true
                binding.customTime.isEnabled = true
            } else {
                binding.timeIntervalET.isEnabled = true
                binding.timeInterval.isEnabled = true
                binding.customTimeET.isEnabled = false
                binding.customTime.isEnabled = false
            }
        }
    }

    private fun setViews() {
        binding.themeSwitch.isChecked = viewModel.isDarkMode ?: false
        binding.timeIntervalET.setText(sharedViewModel.notificationTimeInterval.toString())
    }

    private fun updateTheme(isChecked: Boolean) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (isChecked) viewModel.updateTheme(Theme.NIGHT)
                else viewModel.updateTheme(Theme.LIGHT)
            }
        }
    }

    private fun setDropDownItems() {
        val items = listOf("3", "5", "8", "12")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        binding.timeIntervalET.setAdapter(adapter)
    }

    private fun enqueueWork() {
        val count = sharedViewModel.cartProductsCount
        val interval = sharedViewModel.notificationTimeInterval
        val cartNotificationWorker =
            PeriodicWorkRequestBuilder<NotificationWorker>(interval!!.toLong(), TimeUnit.HOURS)
                .setInputData(workDataOf("count" to count))
                //  .setInitialDelay(10,TimeUnit.MINUTES)
                .build()
        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "cart notification",
            ExistingPeriodicWorkPolicy.REPLACE,
            cartNotificationWorker
        )
    }

    override fun onStop() {
        if (oldTimeInterval != newTimeInterval) enqueueWork()
        super.onStop()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
