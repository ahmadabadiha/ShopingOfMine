package com.example.shopingofmine.data.model.apimodels

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Dimensions(
    @JsonProperty("height")
    val height: String,
    @JsonProperty("length")
    val length: String,
    @JsonProperty("width")
    val width: String
)