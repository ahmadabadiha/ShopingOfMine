package com.example.shopingofmine.model.serverdataclass

data class OrderShipping(
    val address_1: String,
    val address_2: String,
    val city: String,
    val country: String,
    val first_name: String,
    val last_name: String,
    val postcode: String,
    val state: String
)