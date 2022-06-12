package com.example.shopingofmine.data.model.appmodels

data class UpdateOrderClass(
    val line_items: List<Any> = emptyList(),
    val status: String = "pending"
)