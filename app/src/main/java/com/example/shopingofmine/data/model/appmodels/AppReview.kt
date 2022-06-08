package com.example.shopingofmine.data.model.appmodels

import com.example.shopingofmine.data.model.apimodels.Links
import com.example.shopingofmine.data.model.apimodels.ReviewerAvatarUrls
import com.fasterxml.jackson.annotation.JsonProperty

data class AppReview(
    val product_id: Int,
    val rating: Int,
    val review: String,
    val reviewer: String,
    val reviewer_email: String
)
