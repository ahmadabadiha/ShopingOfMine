package com.example.shopingofmine.data.model.appmodels

import androidx.annotation.Keep

@Keep
data class UpdatingOrderClass(
    val line_items: List<Any> = emptyList(),
    val status: String = "pending"
)