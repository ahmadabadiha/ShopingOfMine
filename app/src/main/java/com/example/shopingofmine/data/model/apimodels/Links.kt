package com.example.shopingofmine.data.model.apimodels

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Links(
    @JsonProperty("collection")
    val collection: List<Collection>,
    @JsonProperty("self")
    val self: List<Self>
)