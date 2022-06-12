package com.example.shopingofmine.data.model.apimodels

import com.fasterxml.jackson.annotation.JsonProperty

data class Customer(
    @JsonProperty("_links")
    val _links: Links,
    @JsonProperty("avatar_url")
    val avatar_url: String,
    @JsonProperty("billing")
    val billing: CustomerBilling,
    @JsonProperty("date_created")
    val date_created: String,
    @JsonProperty("date_created_gmt")
    val date_created_gmt: String,
    @JsonProperty("date_modified")
    val date_modified: String,
    @JsonProperty("date_modified_gmt")
    val date_modified_gmt: String,
    @JsonProperty("email")
    val email: String,
    @JsonProperty("first_name")
    val first_name: String,
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("is_paying_customer")
    val is_paying_customer: Boolean,
    @JsonProperty("last_name")
    val last_name: String,
    @JsonProperty("meta_data")
    val meta_data: List<Any>,
    @JsonProperty("role")
    val role: String,
    @JsonProperty("shipping")
    val shipping: CustomerShipping,
    @JsonProperty("username")
    val username: String
)