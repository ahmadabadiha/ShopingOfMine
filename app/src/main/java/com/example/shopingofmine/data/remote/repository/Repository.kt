package com.example.shopingofmine.data.remote.repository

import com.example.shopingofmine.data.model.localdataclass.LocalOrderClass
import com.example.shopingofmine.data.remote.RemoteDataSource
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

    suspend fun getProductsByCategory(categoryId: String, orderBy: String, order: String) = safeApiCall {
        remoteDataSource.getProductsByCategory(categoryId, orderBy, order)
    }

    suspend fun getProductByIds(productIds: Array<Int>) = safeApiCall {
        remoteDataSource.getProductsByIds(productIds.joinToString(","))
    }

    suspend fun addOrder(order: LocalOrderClass) {
        remoteDataSource.addOrder(order)
    }

    suspend fun searchProducts(query: String, orderBy: String, order: String) = safeApiCall {
        remoteDataSource.searchProducts(query, orderBy, order)
    }

    suspend fun getProductReviews(productIds: Array<Int>, perPage: Int) = safeApiCall {
        remoteDataSource.getProductReviews(productIds.joinToString(","), perPage)
    }
}