package com.example.shopingofmine.data.model.apimodels

import com.fasterxml.jackson.annotation.JsonProperty

data class ProductItem(
    @JsonProperty("_links")
    val _links: Links,
    @JsonProperty("attributes")
    val attributes: List<Any>?,
    @JsonProperty("average_rating")
    val average_rating: String,
    @JsonProperty("backordered")
    val backordered: Boolean,
    @JsonProperty("backorders")
    val backorders: String,
    @JsonProperty("backorders_allowed")
    val backorders_allowed: Boolean,
    @JsonProperty("button_text")
    val button_text: String,
    @JsonProperty("catalog_visibility")
    val catalog_visibility: String,
    @JsonProperty("categories")
    val categories: List<Category>?,
    @JsonProperty("cross_sell_ids")
    val cross_sell_ids: List<Any>?,
    @JsonProperty("date_created")
    val date_created: String,
    @JsonProperty("date_created_gmt")
    val date_created_gmt: String,
    @JsonProperty("date_modified")
    val date_modified: String,
    @JsonProperty("date_modified_gmt")
    val date_modified_gmt: String,
    @JsonProperty("date_on_sale_from")
    val date_on_sale_from: Any?,
    @JsonProperty("date_on_sale_from_gmt")
    val date_on_sale_from_gmt: Any?,
    @JsonProperty("date_on_sale_to")
    val date_on_sale_to: Any?,
    @JsonProperty("date_on_sale_to_gmt")
    val date_on_sale_to_gmt: Any?,
    @JsonProperty("default_attributes")
    val default_attributes: List<Any>?,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("dimensions")
    val dimensions: Dimensions,
    @JsonProperty("download_expiry")
    val download_expiry: Int,
    @JsonProperty("download_limit")
    val download_limit: Int,
    @JsonProperty("downloadable")
    val downloadable: Boolean,
    @JsonProperty("downloads")
    val downloads: List<Any>?,
    @JsonProperty("external_url")
    val external_url: String,
    @JsonProperty("featured")
    val featured: Boolean,
    @JsonProperty("grouped_products")
    val grouped_products: List<Any>?,
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("images")
    val images: List<Image>,
    @JsonProperty("low_stock_amount")
    val low_stock_amount: Any?,
    @JsonProperty("manage_stock")
    val manage_stock: Boolean,
    @JsonProperty("menu_order")
    val menu_order: Int,
    @JsonProperty("meta_data")
    val meta_data: List<Any>?,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("on_sale")
    val on_sale: Boolean,
    @JsonProperty("parent_id")
    val parent_id: Int,
    @JsonProperty("permalink")
    val permalink: String,
    @JsonProperty("price")
    val price: String,
    @JsonProperty("price_html")
    val price_html: String,
    @JsonProperty("purchasable")
    val purchasable: Boolean,
    @JsonProperty("purchase_note")
    val purchase_note: String,
    @JsonProperty("rating_count")
    val rating_count: Int,
    @JsonProperty("regular_price")
    val regular_price: String,
    @JsonProperty("related_ids")
    val related_ids: List<Int>,
    @JsonProperty("reviews_allowed")
    val reviews_allowed: Boolean,
    @JsonProperty("sale_price")
    val sale_price: String,
    @JsonProperty("shipping_class")
    val shipping_class: String,
    @JsonProperty("shipping_class_id")
    val shipping_class_id: Int,
    @JsonProperty("shipping_required")
    val shipping_required: Boolean,
    @JsonProperty("shipping_taxable")
    val shipping_taxable: Boolean,
    @JsonProperty("short_description")
    val short_description: String,
    @JsonProperty("sku")
    val sku: String,
    @JsonProperty("slug")
    val slug: String,
    @JsonProperty("sold_individually")
    val sold_individually: Boolean,
    @JsonProperty("status")
    val status: String,
    @JsonProperty("stock_quantity")
    val stock_quantity: Any?,
    @JsonProperty("stock_status")
    val stock_status: String,
    @JsonProperty("tags")
    val tags: List<Tag>,
    @JsonProperty("tax_class")
    val tax_class: String,
    @JsonProperty("tax_status")
    val tax_status: String,
    @JsonProperty("total_sales")
    val total_sales: Int,
    @JsonProperty("type")
    val type: String,
    @JsonProperty("upsell_ids")
    val upsell_ids: List<Any>?,
    @JsonProperty("variations")
    val variations: List<Any>?,
    @JsonProperty("virtual")
    val virtual: Boolean,
    @JsonProperty("weight")
    val weight: String
)