package com.example.shopingofmine.data.model.serverdataclass

import com.fasterxml.jackson.annotation.JsonProperty

data class Self(
    @JsonProperty("href")
    val href: String
)