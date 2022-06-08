package com.example.shopingofmine.data.remote

import com.example.shopingofmine.data.model.appmodels.AppOrderClass
import com.example.shopingofmine.data.model.apimodels.CategoryItem
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.data.model.apimodels.Review
import com.example.shopingofmine.data.model.appmodels.AppReview
import retrofit2.Response

interface RemoteDataSource {
    suspend fun getProducts(orderBy: String): Response<List<ProductItem>>
    suspend fun getCategories(): Response<List<CategoryItem>>
    suspend fun getProductsByCategory(categoryId: String, orderBy: String, order: String): Response<List<ProductItem>>
    suspend fun getProductsByIds(productIds: String): Response<List<ProductItem>>
    suspend fun addOrder(order: AppOrderClass)
    suspend fun searchProducts(query: String, orderBy: String, order: String): Response<List<ProductItem>>
    suspend fun getProductReviews(productIds: String, perPage: Int): Response<List<Review>>
    suspend fun addReview(review: AppReview): Response<Any>
}