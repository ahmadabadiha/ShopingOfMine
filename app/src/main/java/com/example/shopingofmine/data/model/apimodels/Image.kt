package com.example.shopingofmine.data.model.apimodels

import com.fasterxml.jackson.annotation.JsonProperty

data class Image(
    @JsonProperty("alt")
    val alt: String,
    @JsonProperty("date_created")
    val date_created: String,
    @JsonProperty("date_created_gmt")
    val date_created_gmt: String,
    @JsonProperty("date_modified")
    val date_modified: String,
    @JsonProperty("date_modified_gmt")
    val date_modified_gmt: String,
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("src")
    val src: String
)