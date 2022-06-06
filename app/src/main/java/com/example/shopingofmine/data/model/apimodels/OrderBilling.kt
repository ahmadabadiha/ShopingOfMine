package com.example.shopingofmine.data.model.apimodels

import com.fasterxml.jackson.annotation.JsonProperty

data class OrderBilling(
    @JsonProperty("slug")
    val address_1: String,
    @JsonProperty("slug")
    val address_2: String,
    @JsonProperty("slug")
    val city: String,
    @JsonProperty("slug")
    val country: String,
    @JsonProperty("slug")
    val email: String,
    @JsonProperty("slug")
    val first_name: String,
    @JsonProperty("slug")
    val last_name: String,
    @JsonProperty("slug")
    val phone: String,
    @JsonProperty("slug")
    val postcode: String,
    @JsonProperty("slug")
    val state: String
)