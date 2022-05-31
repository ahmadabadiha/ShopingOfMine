package com.example.shopingofmine.model.serverdataclass

import com.fasterxml.jackson.annotation.JsonProperty

data class Self(
    @JsonProperty("href")
    val href: String
)