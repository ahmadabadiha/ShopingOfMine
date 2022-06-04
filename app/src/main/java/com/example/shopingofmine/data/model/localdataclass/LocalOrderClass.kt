package com.example.shopingofmine.data.model.localdataclass

import com.fasterxml.jackson.annotation.JsonProperty

class LocalOrderClass(
    @JsonProperty("line_items")
    val line_items: List<LocalLineItem>,
    @JsonProperty("orderShipping")
    val orderShipping: LocalOrderShipping,
)