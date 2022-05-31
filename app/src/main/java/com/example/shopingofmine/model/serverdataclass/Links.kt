package com.example.shopingofmine.model.serverdataclass

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Links(
    @JsonProperty("collection")
    val collection: List<Collection>,
    @JsonProperty("self")
    val self: List<Self>
)