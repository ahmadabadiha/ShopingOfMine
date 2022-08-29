package com.example.shopingofmine.data.model.appmodels

import androidx.annotation.Keep

@Keep
data class AppShipping(
    val address_1: String,
    val city: String,
    val first_name: String,
    val last_name: String,
    val postcode: String
)