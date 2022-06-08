package com.example.shopingofmine.data.model.appmodels

import com.fasterxml.jackson.annotation.JsonProperty

class AppOrderClass(
    @JsonProperty("line_items")
    val line_items: List<AppLineItem>,
    @JsonProperty("orderShipping")
    val orderShipping: AppOrderShipping,
)