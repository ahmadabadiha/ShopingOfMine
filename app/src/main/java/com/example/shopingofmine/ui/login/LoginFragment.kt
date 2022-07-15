package com.example.shopingofmine.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shopingofmine.R
import com.example.shopingofmine.data.model.appmodels.AppCustomer
import com.example.shopingofmine.data.model.appmodels.AppShipping
import com.example.shopingofmine.data.remote.ResultWrapper
import com.example.shopingofmine.databinding.FragmentLoginBinding
import com.example.shopingofmine.ui.buildAndShowErrorDialog
import com.example.shopingofmine.ui.collectFlow
import com.example.shopingofmine.ui.sharedviewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)
        binding.loginButton.setOnClickListener {
            val shipping = AppShipping(
                first_name = binding.firstName.text.toString(),
                last_name = binding.lastName.text.toString(),
                city = binding.city.text.toString(),
                postcode = binding.postCode.text.toString(),
                address_1 = binding.address.text.toString()
            )
            val customer =
                AppCustomer(binding.email.text.toString(), binding.firstName.text.toString(), binding.lastName.text.toString(), shipping)
            viewLifecycleOwner.lifecycleScope.launch {
                createCustomer(customer)
            }
        }
    }

    private suspend fun createCustomer(customer: AppCustomer) {
        collectFlow(viewModel.createCustomer(customer)) {
            when (it) {
                ResultWrapper.Loading -> {
                    binding.loadingAnim.playAnimation()
                    binding.loadingAnim.isGone = false
                }
                is ResultWrapper.Success -> {
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                    Toast.makeText(requireContext(), "ثبت نام شما با موفقیت انجام شد.", Toast.LENGTH_SHORT).show()
                    sharedViewModel.customerId = it.value.id
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToProductDetailsFragment(sharedViewModel.productItem.id))
                }
                is ResultWrapper.Error -> {
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                    buildAndShowErrorDialog(it.message, " خطا در ساختن مشتری") {
                        viewLifecycleOwner.lifecycleScope.launch { createCustomer(customer) }
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