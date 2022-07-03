package com.example.shopingofmine.data.model.apimodels

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ShippingLine(
    @JsonProperty("method_id")
    val method_id: String,
    @JsonProperty("method_title")
    val method_title: String,
    @JsonProperty("total")
    val total: String
)