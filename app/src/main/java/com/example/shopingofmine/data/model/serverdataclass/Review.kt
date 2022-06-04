package com.example.shopingofmine.data.model.serverdataclass

import com.fasterxml.jackson.annotation.JsonProperty

data class Review(
    @JsonProperty("_links")
    val _links: Links,
    @JsonProperty("date_created")
    val date_created: String,
    @JsonProperty("date_created_gmt")
    val date_created_gmt: String,
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("product_id")
    val product_id: Int,
    @JsonProperty("rating")
    val rating: Int,
    @JsonProperty("review")
    val review: String,
    @JsonProperty("reviewer")
    val reviewer: String,
    @JsonProperty("reviewer_avatar_urls")
    val reviewer_avatar_urls: ReviewerAvatarUrls,
    @JsonProperty("reviewer_email")
    val reviewer_email: String,
    @JsonProperty("status")
    val status: String,
    @JsonProperty("verified")
    val verified: Boolean
)