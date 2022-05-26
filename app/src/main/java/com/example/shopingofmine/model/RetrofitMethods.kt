package com.example.shopingofmine.model

import com.example.shopingofmine.model.serverdataclass.CategoryItem
import com.example.shopingofmine.model.serverdataclass.ProductItem
import com.example.shopingofmine.util.CONSUMER_KEY
import com.example.shopingofmine.util.CONSUMER_SECRET
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitMethods {
    @GET("products")
    suspend fun getProducts(
        @Query("orderby") orderBy: String,
        @Query("consumer_key") consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret") consumerSecret: String = CONSUMER_SECRET
    ): Response<List<ProductItem>>

    @GET("products/categories")
    suspend fun getCategories(
        @Query("consumer_key") consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret") consumerSecret: String = CONSUMER_SECRET
    ): Response<List<CategoryItem>>

    @GET("products")
    suspend fun getProductsByCategory(
        @Query("category") categoryId: String,
        @Query("consumer_key") consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret") consumerSecret: String = CONSUMER_SECRET
    ): Response<List<ProductItem>>

}