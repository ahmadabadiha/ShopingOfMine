package com.example.shopingofmine.model

import com.example.shopingofmine.model.localdataclass.LocalOrderClass
import com.example.shopingofmine.model.serverdataclass.OrderClass
import com.example.shopingofmine.util.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val remoteDataSource: RemoteDataSource) {
    suspend fun getProducts(orderBy: String) = safeApiCall {
        remoteDataSource.getProducts(orderBy)
    }

    suspend fun getCategories() = safeApiCall {
        remoteDataSource.getCategories()
    }

    suspend fun getProductsByCategory(categoryId: String, orderBy: String) = safeApiCall {
        remoteDataSource.getProductsByCategory(categoryId, orderBy)
    }

    suspend fun getProductByIds(productIds: Array<Int>) = safeApiCall {
        remoteDataSource.getProductsByIds(productIds.joinToString(","))
    }

    suspend fun addOrder(order: LocalOrderClass) {
        remoteDataSource.addOrder(order)
    }

    suspend fun searchProducts(query: String, orderBy: String) = safeApiCall {
        remoteDataSource.searchProducts(query, orderBy)
    }
}