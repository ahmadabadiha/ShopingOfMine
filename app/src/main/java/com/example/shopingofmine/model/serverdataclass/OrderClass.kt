package com.example.shopingofmine.model.serverdataclass

data class OrderClass(
    val orderBilling: OrderBilling,
    val line_items: List<LineItem>,
    val payment_method: String,
    val payment_method_title: String,
    val set_paid: Boolean,
    val orderShipping: OrderShipping,
    val shipping_lines: List<ShippingLine>
)