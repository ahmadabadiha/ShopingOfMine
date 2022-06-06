package com.example.shopingofmine.data.model.apimodels

import com.fasterxml.jackson.annotation.JsonProperty

data class Dimensions(
    @JsonProperty("height")
    val height: String,
    @JsonProperty("length")
    val length: String,
    @JsonProperty("width")
    val width: String
)