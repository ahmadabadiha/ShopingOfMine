package com.example.shopingofmine.data.model.appmodels

import com.fasterxml.jackson.annotation.JsonProperty

class AppLineItem(
    @JsonProperty("product_id")
    val product_id: Int,
    @JsonProperty("quantity")
    val quantity: Int)