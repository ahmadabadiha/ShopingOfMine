package com.example.shopingofmine.model.localdataclass

import com.fasterxml.jackson.annotation.JsonProperty

class LocalOrderShipping (
    @JsonProperty("address_1")
    val address_1: String,
    @JsonProperty("city")
    val city: String,
    @JsonProperty("first_name")
    val first_name: String,
    @JsonProperty("last_name")
    val last_name: String,
    @JsonProperty("postcode")
    val postcode: String,
)