package com.example.shopingofmine.data.model.apimodels

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class OrderLineItem(
    @JsonProperty("id:")
    val id: Int,
    @JsonProperty("meta_data")
    val meta_data: List<Any>,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("parent_name")
    val parent_name: Any?,
    @JsonProperty("price")
    val price: Int,
    @JsonProperty("product_id")
    val product_id: Int,
    @JsonProperty("quantity")
    val quantity: Int,
    @JsonProperty("sku")
    val sku: String,
    @JsonProperty("subtotal")
    val subtotal: String,
    @JsonProperty("subtotal_tax")
    val subtotal_tax: String,
    @JsonProperty("tax_class")
    val tax_class: String,
    @JsonProperty("taxes")
    val taxes: List<Any>,
    @JsonProperty("total")
    val total: String,
    @JsonProperty("total_tax")
    val total_tax: String,
    @JsonProperty("variation_id")
    val variation_id: Int
)