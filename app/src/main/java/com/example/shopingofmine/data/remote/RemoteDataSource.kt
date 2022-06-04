package com.example.shopingofmine.data.remote

import com.example.shopingofmine.data.model.localdataclass.LocalOrderClass
import com.example.shopingofmine.data.model.serverdataclass.CategoryItem
import com.example.shopingofmine.data.model.serverdataclass.ProductItem
import retrofit2.Response

interface RemoteDataSource {
    suspend fun getProducts(orderBy: String): Response<List<ProductItem>>
    suspend fun getCategories(): Response<List<CategoryItem>>
    suspend fun getProductsByCategory(categoryId: String, orderBy: String): Response<List<ProductItem>>
    suspend fun getProductsByIds(productIds: String): Response<List<ProductItem>>
    suspend fun addOrder (order: LocalOrderClass)
    suspend fun searchProducts (query: String, orderBy: String): Response<List<ProductItem>>
}