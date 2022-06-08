package com.example.shopingofmine.data.remote

import com.example.shopingofmine.data.model.appmodels.AppOrderClass
import com.example.shopingofmine.data.model.apimodels.CategoryItem
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.data.model.apimodels.Review
import com.example.shopingofmine.data.model.appmodels.AppReview
import com.example.shopingofmine.util.CONSUMER_KEY
import com.example.shopingofmine.util.CONSUMER_SECRET
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitService {
    @GET("products")
    suspend fun getProducts(
        @Query("orderby") orderBy: String,
        @Query("consumer_key") consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret") consumerSecret: String = CONSUMER_SECRET
    ): Response<List<ProductItem>>

    @GET("products")
    suspend fun searchProducts(
        @Query("search") query: String,
        @Query("orderby") orderBy: String,
        @Query("order") order: String,
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
        @Query("orderby") orderBy: String,
        @Query("order") order: String,
        @Query("exclude") excludeId: String = "608",
        @Query("consumer_key") consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret") consumerSecret: String = CONSUMER_SECRET
    ): Response<List<ProductItem>>

    @GET("products")
    suspend fun getProductsByIds(
        @Query("include") productIds: String,
        @Query("consumer_key") consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret") consumerSecret: String = CONSUMER_SECRET
    ): Response<List<ProductItem>>

    @POST("orders")
    suspend fun addOrder(
        @Body order: AppOrderClass,
        @Query("consumer_key") consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret") consumerSecret: String = CONSUMER_SECRET
    )

    @GET("products/reviews")
    suspend fun getProductsReviews(
        @Query("product") productIds: String,
        @Query("per_page") perPage: Int,
        @Query("consumer_key") consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret") consumerSecret: String = CONSUMER_SECRET
    ): Response<List<Review>>

    @POST("products/reviews")
    suspend fun addReview(
        @Body order: AppReview,
        @Query("consumer_key") consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret") consumerSecret: String = CONSUMER_SECRET
    ): Response<Any>
}