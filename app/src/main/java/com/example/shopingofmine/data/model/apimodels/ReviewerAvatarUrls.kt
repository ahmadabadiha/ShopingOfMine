package com.example.shopingofmine.data.model.apimodels

import com.fasterxml.jackson.annotation.JsonProperty

data class ReviewerAvatarUrls(
    @JsonProperty("24")
    val `24`: String,
    @JsonProperty("48")
    val `48`: String,
    @JsonProperty("96")
    val `96`: String
)