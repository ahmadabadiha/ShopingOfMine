package com.example.shopingofmine.model.serverdataclass

data class Customer(
    val billing: CustomerBilling,
    val email: String,
    val first_name: String,
    val last_name: String,
    val shipping: CustomerShipping,
    val username: String
)