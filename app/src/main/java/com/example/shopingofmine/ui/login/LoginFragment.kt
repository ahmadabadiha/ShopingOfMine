package com.example.shopingofmine.ui.login

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shopingofmine.R
import com.example.shopingofmine.ShoppingNavDirections
import com.example.shopingofmine.data.model.appmodels.AppCustomer
import com.example.shopingofmine.data.model.appmodels.AppShipping
import com.example.shopingofmine.data.remote.ResultWrapper
import com.example.shopingofmine.databinding.FragmentLoginBinding
import com.example.shopingofmine.ui.buildAndShowErrorDialog
import com.example.shopingofmine.ui.collectFlow
import com.example.shopingofmine.ui.sharedviewmodel.SharedViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        if (sharedViewModel.customerId != null) {
            binding.logoutLayout.isGone = false
        } else binding.signInLayout.isGone = false
        initSetOnClickListeners()
        initSetFragmentResultListener()
    }

    private fun initSetFragmentResultListener() {
        setFragmentResultListener("coordinates") { _, bundle ->
            val lat = String.format("%.3f", bundle.getDouble("lat"))
            val lon = String.format("%.3f", bundle.getDouble("lon"))
            Toast.makeText(requireContext(), "عرض: $lat, طول: $lon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initSetOnClickListeners() {
        binding.logoutButton.setOnClickListener {
            val alertDialog: AlertDialog? = activity?.let {
                MaterialAlertDialogBuilder(it, R.style.AlertDialogCustom)
            }?.setMessage("آیا می خواهید از حساب کاربری خود خارج شوید؟")
                ?.setTitle("خروج؟")
                ?.setIcon(R.drawable.ic_round_how_to_reg_24)
                ?.setPositiveButton("بله") { _, _ ->
                    sharedViewModel.customerId = null
                    binding.logoutLayout.isGone = true
                    binding.signInLayout.isGone = false
                }
                ?.setNegativeButton("انصراف") { _, _ ->
                }?.create()
            alertDialog?.show()
        }
        binding.registerButton.setOnClickListener {
            if (isValidEmail(binding.email.text!!)) {
                val shipping = AppShipping(
                    first_name = binding.firstName.text.toString(),
                    last_name = binding.lastName.text.toString(),
                    city = binding.city.text.toString(),
                    postcode = binding.postCode.text.toString(),
                    address_1 = binding.address.text.toString()
                )
                val customer =
                    AppCustomer(
                        binding.email.text.toString(),
                        binding.firstName.text.toString(),
                        binding.lastName.text.toString(),
                        shipping
                    )
                viewLifecycleOwner.lifecycleScope.launch {
                    createCustomer(customer)
                }
            } else Toast.makeText(requireContext(), "آدرس ایمیل معتبر نمی باشد.", Toast.LENGTH_SHORT).show()
        }
        binding.mapButton.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMapFragment())
        }
        binding.signupClickableTV.setOnClickListener {
            binding.signInLayout.isGone = true
            binding.signUpLayout.isGone = false
        }
        binding.loginButton.setOnClickListener {
            if (isValidEmail(binding.loginMail.text!!)) {
                viewLifecycleOwner.lifecycleScope.launch {
                    findCustomer(binding.loginMail.text.toString())
                }
            } else Toast.makeText(requireContext(), "آدرس ایمیل معتبر نمی باشد.", Toast.LENGTH_SHORT).show()
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
                    if (sharedViewModel.isProductItemInitialized()) {
                        findNavController().navigate(ShoppingNavDirections.actionGlobalProductDetailsFragment(sharedViewModel.productItem.id))
                    } else findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
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

    private suspend fun findCustomer(email: String) {
        collectFlow(viewModel.findCustomer(email)) {
            when (it) {
                ResultWrapper.Loading -> {
                    binding.loadingAnim.playAnimation()
                    binding.loadingAnim.isGone = false
                }
                is ResultWrapper.Success -> {
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                    if (it.value.isNotEmpty()) {
                        Toast.makeText(requireContext(), "ورود شما با موفقیت انجام شد.", Toast.LENGTH_SHORT).show()
                        sharedViewModel.customerId = it.value[0].id
                        if (sharedViewModel.isProductItemInitialized()) {
                            findNavController().navigate(ShoppingNavDirections.actionGlobalProductDetailsFragment(sharedViewModel.productItem.id))
                        } else findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                    } else Toast.makeText(requireContext(), "کاربری با این ایمیل یافت نشد.", Toast.LENGTH_SHORT).show()
                }
                is ResultWrapper.Error -> {
                    binding.loadingAnim.pauseAnimation()
                    binding.loadingAnim.isGone = true
                    buildAndShowErrorDialog(it.message, " خطا در فرایند ورود") {
                        viewLifecycleOwner.lifecycleScope.launch { findCustomer(email) }
                    }
                }
            }
        }
    }

    private fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}