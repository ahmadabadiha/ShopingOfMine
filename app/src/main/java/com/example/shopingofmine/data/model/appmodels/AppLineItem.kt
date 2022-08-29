package com.example.shopingofmine.data.model.appmodels

import androidx.annotation.Keep

@Keep
data class AppLineItem(
    val product_id: Int,
    val quantity: Int)