package com.example.shopingofmine.model

import com.example.shopingofmine.model.serverdataclass.CategoryItem
import com.example.shopingofmine.model.serverdataclass.ProductItem
import retrofit2.Response

interface RemoteDataSource {
    suspend fun getProducts(orderBy: String): Response<List<ProductItem>>
    suspend fun getCategories(): Response<List<CategoryItem>>
    suspend fun getProductsByCategory(categoryId: String): Response<List<ProductItem>>
}