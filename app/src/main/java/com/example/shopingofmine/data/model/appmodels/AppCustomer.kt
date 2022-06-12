package com.example.shopingofmine.data.model.appmodels

data class AppCustomer(
    val email: String,
    val first_name: String,
    val last_name: String,
    val shipping: AppShipping
)