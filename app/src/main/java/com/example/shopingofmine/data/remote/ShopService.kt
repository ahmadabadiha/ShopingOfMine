package com.example.shopingofmine.data.remote

import com.example.shopingofmine.data.model.apimodels.*
import com.example.shopingofmine.data.model.appmodels.AppCustomer
import com.example.shopingofmine.data.model.appmodels.AppOrderClass
import com.example.shopingofmine.data.model.appmodels.AppReview
import com.example.shopingofmine.data.model.appmodels.UpdatingOrderClass
import com.example.shopingofmine.util.CONSUMER_KEY
import com.example.shopingofmine.util.CONSUMER_SECRET
import retrofit2.Response
import retrofit2.http.*

interface ShopService {
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
        @Query("per_page") perPage: Int = 50,
        @Query("consumer_key") consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret") consumerSecret: String = CONSUMER_SECRET
    ): Response<List<ProductItem>>

    @GET("products/categories")
    suspend fun getCategories(
        @Query("per_page") perPage: Int = 50,
        @Query("consumer_key") consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret") consumerSecret: String = CONSUMER_SECRET
    ): Response<List<CategoryItem>>

    @GET("products")
    suspend fun getProductsByCategory(
        @Query("category") categoryId: String,
        @Query("orderby") orderBy: String,
        @Query("order") order: String,
        @Query("exclude") excludeId: String = "608",
        @Query("per_page") perPage: Int = 50,
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
    ): Response<Order>

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

    @POST("customers")
    suspend fun createCustomer(
        @Body customer: AppCustomer,
        @Query("consumer_key") consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret") consumerSecret: String = CONSUMER_SECRET
    ): Response<Customer>

    @GET("customers/{id}")
    suspend fun getCustomer(
        @Path("id") id: Int,
        @Query("consumer_key") consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret") consumerSecret: String = CONSUMER_SECRET
    ): Response<Customer>

    @GET("orders")
    suspend fun getCustomerOrders(
        @Query("customer") customerId: Int,
        @Query("status") status: String,
        @Query("consumer_key") consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret") consumerSecret: String = CONSUMER_SECRET
    ): Response<List<Order>>

    @PUT("orders/{id}")
    suspend fun updateOrder(
        @Path("id") id: Int,
        @Body updatedOrder: UpdatingOrderClass,
        @Query("consumer_key") consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret") consumerSecret: String = CONSUMER_SECRET
    ): Response<Order>

    @GET("coupons")
    suspend fun getCoupon(
        @Query("code") code: String,
        @Query("consumer_key") consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret") consumerSecret: String = CONSUMER_SECRET
    ): Response<List<Coupon>>
}