package com.example.shopingofmine.model

import com.example.shopingofmine.model.serverdataclass.CategoryItem
import com.example.shopingofmine.model.serverdataclass.OrderClass
import com.example.shopingofmine.model.serverdataclass.ProductItem
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

    override suspend fun getProductsByCategory(categoryId: String): Response<List<ProductItem>> {
        return retrofitMethods.getProductsByCategory(categoryId)

    }

    override suspend fun getProductsByIds(productIds: Array<Int>): Response<List<ProductItem>> {
        return retrofitMethods.getProductsByIds(productIds)
    }

    override suspend fun addOrder(order: OrderClass) {
        retrofitMethods.addOrder(order)
    }
}