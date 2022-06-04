package com.example.shopingofmine.data.model.serverdataclass

import com.fasterxml.jackson.annotation.JsonProperty

data class OrderShipping(
    @JsonProperty("address_1")
    val address_1: String,
    @JsonProperty("address_2")
    val address_2: String,
    @JsonProperty("city")
    val city: String,
    @JsonProperty("country")
    val country: String,
    @JsonProperty("first_name")
    val first_name: String,
    @JsonProperty("last_name")
    val last_name: String,
    @JsonProperty("postcode")
    val postcode: String,
    @JsonProperty("state")
    val state: String
)