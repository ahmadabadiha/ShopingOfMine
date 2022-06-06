package com.example.shopingofmine.data.model.apimodels

import com.fasterxml.jackson.annotation.JsonProperty

data class Collection(
    @JsonProperty("href")
    val href: String
)