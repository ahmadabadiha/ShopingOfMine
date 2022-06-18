package com.example.shopingofmine.ui.completeorder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopingofmine.data.model.apimodels.Order
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.data.model.appmodels.UpdatingOrderClass
import com.example.shopingofmine.data.remote.ResultWrapper
import com.example.shopingofmine.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class CompleteOrderViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    suspend fun updateOrder(orderId: Int): SharedFlow<ResultWrapper<Order>> {
       return repository.updateOrder(orderId,UpdatingOrderClass(status = "completed")).shareIn(viewModelScope, SharingStarted.Lazily)
    }

    fun computePriceWithDiscount(cartItems: List<ProductItem>, productsCount: List<Int>) =
        cartItems.foldIndexed(0) { index, acc, productItem ->
            acc + productItem.price.toInt() * productsCount[index]
        }


    fun computeDiscount(cartItems: List<ProductItem>, productsCount: List<Int>): Pair<Int, Int> {
        val discountAmount = cartItems.foldIndexed(0) { index, acc, productItem ->
            acc + (productItem.regular_price.toInt() - productItem.price.toInt()) * productsCount[index]
        }

        val totalRegularPrice = cartItems.foldIndexed(0) { index, acc, productItem ->
            acc + productItem.regular_price.toInt() * productsCount[index]
        }

        val discountPercent = ((discountAmount.toDouble() / totalRegularPrice.toDouble()) * 100).toInt()
        return Pair(discountAmount, discountPercent)
    }

    fun computePriceWithoutDiscount(cartItems: List<ProductItem>, productsCount: List<Int>) =
        cartItems.foldIndexed(0) { index, acc, productItem ->
            acc + productItem.regular_price.toInt() * productsCount[index]
        }
}