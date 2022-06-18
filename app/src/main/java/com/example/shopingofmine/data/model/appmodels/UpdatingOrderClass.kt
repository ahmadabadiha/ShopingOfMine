package com.example.shopingofmine.data.model.appmodels

data class UpdatingOrderClass(
    val line_items: List<Any> = emptyList(),
    val status: String = "pending"
)