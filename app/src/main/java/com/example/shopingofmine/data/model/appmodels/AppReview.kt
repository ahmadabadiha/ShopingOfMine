package com.example.shopingofmine.data.model.appmodels

import androidx.annotation.Keep

@Keep
data class AppReview(
    val product_id: Int,
    val rating: Int,
    val review: String,
    val reviewer: String,
    val reviewer_email: String
)
