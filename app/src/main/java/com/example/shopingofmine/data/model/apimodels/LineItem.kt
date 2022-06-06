package com.example.shopingofmine.data.model.apimodels

import com.fasterxml.jackson.annotation.JsonProperty

data class LineItem(
    @JsonProperty("product_id")
    val product_id: Int,
    @JsonProperty("quantity")
    val quantity: Int,
    @JsonProperty("variation_id")
    val variation_id: Int
)