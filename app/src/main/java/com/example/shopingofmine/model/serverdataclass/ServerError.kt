package com.example.shopingofmine.model.serverdataclass

data class ServerError(
    val code: String,
    val `data`: Data,
    val message: String
)