package com.example.shopingofmine.data.model.apimodels

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ServerError(
    @JsonProperty("code")
    val code: String,
    @JsonProperty("data")
    val `data`: Data,
    @JsonProperty("message")
    val message: String
)