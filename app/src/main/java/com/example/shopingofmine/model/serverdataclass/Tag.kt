package com.example.shopingofmine.model.serverdataclass

import com.fasterxml.jackson.annotation.JsonProperty

data class Tag(
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("slug")
    val slug: String
)