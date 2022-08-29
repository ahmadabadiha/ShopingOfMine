package com.example.shopingofmine.data.model.appmodels

import androidx.annotation.Keep

@Keep
data class UpdatingLineItem(
    val id: Int,
    val product_id: Int,
    val quantity: Int)