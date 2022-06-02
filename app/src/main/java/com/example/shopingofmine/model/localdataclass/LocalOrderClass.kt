package com.example.shopingofmine.model.localdataclass

import com.example.shopingofmine.model.serverdataclass.LineItem
import com.example.shopingofmine.model.serverdataclass.OrderShipping
import com.fasterxml.jackson.annotation.JsonProperty

class LocalOrderClass(
    @JsonProperty("line_items")
    val line_items: List<LocalLineItem>,
    @JsonProperty("orderShipping")
    val orderShipping: LocalOrderShipping,
)