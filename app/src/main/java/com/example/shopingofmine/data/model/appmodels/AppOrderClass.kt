package com.example.shopingofmine.data.model.appmodels

import androidx.annotation.Keep

@Keep
data class AppOrderClass(
    val customer_id: Int,
    val line_items: List<AppLineItem>,
    val shipping: AppShipping
)
