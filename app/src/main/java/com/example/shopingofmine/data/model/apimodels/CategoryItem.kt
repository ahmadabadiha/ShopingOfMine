package com.example.shopingofmine.data.model.apimodels

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class CategoryItem(
    @JsonProperty("_links")
    val _links: Links,
    @JsonProperty("count")
    val count: Int,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("display")
    val display: String,
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("image")
    val image: Image,
    @JsonProperty("menu_order")
    val menu_order: Int,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("parent")
    val parent: Int,
    @JsonProperty("slug")
    val slug: String
)