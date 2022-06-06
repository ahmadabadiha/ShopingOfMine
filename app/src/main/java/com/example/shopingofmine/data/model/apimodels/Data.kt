package com.example.shopingofmine.data.model.apimodels

import com.fasterxml.jackson.annotation.JsonProperty

data class Data(
    @JsonProperty("status")
    val status: Int
)