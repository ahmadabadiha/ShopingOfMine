package com.example.shopingofmine.data.model.serverdataclass

import com.fasterxml.jackson.annotation.JsonProperty

data class Customer(
    @JsonProperty("billing")
    val billing: CustomerBilling,
    @JsonProperty("email")
    val email: String,
    @JsonProperty("first_name")
    val first_name: String,
    @JsonProperty("last_name")
    val last_name: String,
    @JsonProperty("shipping")
    val shipping: CustomerShipping,
    @JsonProperty("username")
    val username: String
)