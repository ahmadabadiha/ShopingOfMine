package com.example.shopingofmine.model.serverdataclass

data class Review(
    val _links: Links,
    val date_created: String,
    val date_created_gmt: String,
    val id: Int,
    val product_id: Int,
    val rating: Int,
    val review: String,
    val reviewer: String,
    val reviewer_avatar_urls: ReviewerAvatarUrls,
    val reviewer_email: String,
    val status: String,
    val verified: Boolean
)