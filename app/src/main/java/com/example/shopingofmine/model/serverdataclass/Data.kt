package com.example.shopingofmine.model.serverdataclass

import com.fasterxml.jackson.annotation.JsonProperty

data class Data(
    @JsonProperty("status")
    val status: Int
)