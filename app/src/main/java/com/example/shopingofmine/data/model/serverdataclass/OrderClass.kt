package com.example.shopingofmine.data.model.serverdataclass

import com.fasterxml.jackson.annotation.JsonProperty

data class OrderClass(
    @JsonProperty("orderBilling")
    val orderBilling: OrderBilling,
    @JsonProperty("line_items")
    val line_items: List<LineItem>,
    @JsonProperty("payment_method")
    val payment_method: String,
    @JsonProperty("payment_method_title")
    val payment_method_title: String,
    @JsonProperty("set_paid")
    val set_paid: Boolean,
    @JsonProperty("orderShipping")
    val orderShipping: OrderShipping,
    @JsonProperty("shipping_lines")
    val shipping_lines: List<ShippingLine>
)