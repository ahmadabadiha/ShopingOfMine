package com.example.shopingofmine.data.model.serverdataclass

import com.fasterxml.jackson.annotation.JsonProperty

data class ServerError(
    @JsonProperty("code")
    val code: String,
    @JsonProperty("data")
    val `data`: Data,
    @JsonProperty("message")
    val message: String
)