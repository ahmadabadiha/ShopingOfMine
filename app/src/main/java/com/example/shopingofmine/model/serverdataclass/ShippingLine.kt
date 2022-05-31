package com.example.shopingofmine.model.serverdataclass

import com.fasterxml.jackson.annotation.JsonProperty

data class ShippingLine(
    @JsonProperty("method_id")
    val method_id: String,
    @JsonProperty("method_title")
    val method_title: String,
    @JsonProperty("total")
    val total: String
)