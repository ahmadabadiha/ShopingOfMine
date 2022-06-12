package com.example.shopingofmine.data.model.appmodels

import com.example.shopingofmine.data.model.apimodels.Customer
import com.example.shopingofmine.data.model.apimodels.CustomerShipping

data class AppOrderClass(
    val customer_id: Int,
    val line_items: List<AppLineItem>,
    val shipping: AppShipping
)
