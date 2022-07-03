package com.example.shopingofmine.data.model.apimodels

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Order(
    @JsonProperty("_links")
    val _links: Links,
    @JsonProperty("billing")
    val billing: CustomerBilling,
    @JsonProperty("cart_hash")
    val cart_hash: String,
    @JsonProperty("cart_tax")
    val cart_tax: String,
    @JsonProperty("coupon_lines")
    val coupon_lines: List<Any>,
    @JsonProperty("created_via")
    val created_via: String,
    @JsonProperty("currency")
    val currency: String,
    @JsonProperty("currency_symbol")
    val currency_symbol: String,
    @JsonProperty("customer_id")
    val customer_id: Int,
    @JsonProperty("customer_ip_address")
    val customer_ip_address: String,
    @JsonProperty("customer_note")
    val customer_note: String,
    @JsonProperty("customer_user_agent")
    val customer_user_agent: String,
    @JsonProperty("date_completed")
    val date_completed: Any?,
    @JsonProperty("date_completed_gmt")
    val date_completed_gmt: Any?,
    @JsonProperty("date_created")
    val date_created: String,
    @JsonProperty("date_created_gmt")
    val date_created_gmt: String,
    @JsonProperty("date_modified")
    val date_modified: String,
    @JsonProperty("date_modified_gmt")
    val date_modified_gmt: String,
    @JsonProperty("date_paid")
    val date_paid: Any?,
    @JsonProperty("date_paid_gmt")
    val date_paid_gmt: Any?,
    @JsonProperty("discount_tax")
    val discount_tax: String,
    @JsonProperty("discount_total")
    val discount_total: String,
    @JsonProperty("fee_lines")
    val fee_lines: List<Any>,
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("line_items")
    val line_items: List<OrderLineItem>,
    @JsonProperty("meta_data")
    val meta_data: List<Any>,
    @JsonProperty("number")
    val number: String,
    @JsonProperty("order_key")
    val order_key: String,
    @JsonProperty("parent_id")
    val parent_id: Int,
    @JsonProperty("payment_method")
    val payment_method: String,
    @JsonProperty("payment_method_title")
    val payment_method_title: String,
    @JsonProperty("prices_include_tax")
    val prices_include_tax: Boolean,
    @JsonProperty("refunds")
    val refunds: List<Any>,
    @JsonProperty("shipping")
    val shipping: CustomerShipping,
    @JsonProperty("shipping_lines")
    val shipping_lines: List<Any>,
    @JsonProperty("shipping_tax")
    val shipping_tax: String,
    @JsonProperty("shipping_total")
    val shipping_total: String,
    @JsonProperty("status")
    val status: String,
    @JsonProperty("tax_lines")
    val tax_lines: List<Any>,
    @JsonProperty("total")
    val total: String,
    @JsonProperty("total_tax")
    val total_tax: String,
    @JsonProperty("transaction_id")
    val transaction_id: String,
    @JsonProperty("version")
    val version: String
)