package com.example.shopingofmine.data.model.apimodels

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Coupon(
    @JsonProperty("_links")
    val _links: Links,
    @JsonProperty("amount")
    val amount: String,
    @JsonProperty("code")
    val code: String,
    @JsonProperty("date_created")
    val date_created: String,
    @JsonProperty("date_created_gmt")
    val date_created_gmt: String,
    @JsonProperty("date_expires")
    val date_expires: Any?,
    @JsonProperty("date_expires_gmt")
    val date_expires_gmt: Any?,
    @JsonProperty("date_modified")
    val date_modified: String,
    @JsonProperty("date_modified_gmt")
    val date_modified_gmt: String,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("discount_type")
    val discount_type: String,
    @JsonProperty("email_restrictions")
    val email_restrictions: List<Any>,
    @JsonProperty("exclude_sale_items")
    val exclude_sale_items: Boolean,
    @JsonProperty("excluded_product_categories")
    val excluded_product_categories: List<Any>,
    @JsonProperty("excluded_product_ids")
    val excluded_product_ids: List<Any>,
    @JsonProperty("free_shipping")
    val free_shipping: Boolean,
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("individual_use")
    val individual_use: Boolean,
    @JsonProperty("limit_usage_to_x_items")
    val limit_usage_to_x_items: Any?,
    @JsonProperty("maximum_amount")
    val maximum_amount: String,
    @JsonProperty("meta_data")
    val meta_data: List<Any>,
    @JsonProperty("minimum_amount")
    val minimum_amount: String,
    @JsonProperty("product_categories")
    val product_categories: List<Any>,
    @JsonProperty("product_ids")
    val product_ids: List<Any>,
    @JsonProperty("usage_count")
    val usage_count: Int,
    @JsonProperty("usage_limit")
    val usage_limit: Any?,
    @JsonProperty("usage_limit_per_user")
    val usage_limit_per_user: Any?,
    @JsonProperty("used_by")
    val used_by: List<Any>
)