package com.example.shopingofmine.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopingofmine.data.remote.repository.Repository
import com.example.shopingofmine.data.model.serverdataclass.ProductItem
import com.example.shopingofmine.data.remote.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    lateinit var productIds: Array<Int>

    private val _cartProducts = MutableStateFlow<ResultWrapper<List<ProductItem>>>(ResultWrapper.Loading)
    val cartProducts = _cartProducts.asStateFlow()

    fun getCartProducts() = viewModelScope.launch {
        repository.getProductByIds(productIds).collectLatest {
            _cartProducts.emit(it)
        }
    }

    fun priceComputerWithDiscount(cartItems: MutableMap<ProductItem, Int>) =
        cartItems.map {
            it.key.price.toInt() * it.value
        }.sum()

    fun computeDiscount(cartItems: MutableMap<ProductItem, Int>): Pair<Int, Int> {
        val discountAmount = cartItems.map {
            (it.key.regular_price.toInt() - it.key.price.toInt()) * it.value
        }.sum()
        val totalRegularPrice = cartItems.map {
            it.key.regular_price.toInt() * it.value
        }.sum()
        val discountPercent = ((discountAmount.toDouble() / totalRegularPrice.toDouble()) * 100).toInt()
        return Pair(discountAmount, discountPercent)
    }

    fun ComputePriceWithoutDiscount(cartItems: MutableMap<ProductItem, Int>) =
        cartItems.map {
            it.key.regular_price.toInt() * it.value
        }.sum()
}