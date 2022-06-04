package com.example.shopingofmine.data.remote

import com.example.shopingofmine.data.model.localdataclass.LocalOrderClass
import com.example.shopingofmine.data.model.serverdataclass.CategoryItem
import com.example.shopingofmine.data.model.serverdataclass.ProductItem
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitDataSource @Inject constructor(private val retrofitMethods: RetrofitMethods) : RemoteDataSource {

    override suspend fun getProducts(orderBy: String): Response<List<ProductItem>> {
        return retrofitMethods.getProducts(orderBy)
    }

    override suspend fun getCategories(): Response<List<CategoryItem>> {
        return retrofitMethods.getCategories()
    }

    override suspend fun getProductsByCategory(categoryId: String, orderBy: String): Response<List<ProductItem>> {
        return retrofitMethods.getProductsByCategory(categoryId,orderBy)

    }

    override suspend fun getProductsByIds(productIds: String): Response<List<ProductItem>> {
        return retrofitMethods.getProductsByIds(productIds)
    }

    override suspend fun addOrder(order: LocalOrderClass) {
        retrofitMethods.addOrder(order)
    }

    override suspend fun searchProducts(query: String, orderBy: String): Response<List<ProductItem>> {
        return retrofitMethods.searchProducts(query, orderBy)
    }
}