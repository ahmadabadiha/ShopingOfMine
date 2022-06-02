package com.example.shopingofmine.model.localdataclass

import com.fasterxml.jackson.annotation.JsonProperty

class LocalLineItem(
    @JsonProperty("product_id")
    val product_id: Int,
    @JsonProperty("quantity")
    val quantity: Int)