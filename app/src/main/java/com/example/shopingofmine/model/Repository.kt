package com.example.shopingofmine.model

import com.example.shopingofmine.model.serverdataclass.OrderClass
import com.example.shopingofmine.util.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val remoteDataSource: RemoteDataSource) {
    suspend fun getProducts(orderBy: String)= safeApiCall{
        remoteDataSource.getProducts(orderBy)
    }

    suspend fun getCategories ()= safeApiCall{
        remoteDataSource.getCategories()
    }

    suspend fun getProductsByCategory(categoryId: String) = safeApiCall {
        remoteDataSource.getProductsByCategory(categoryId)
    }

    suspend fun getProductByIds (productIds: Array<Int>) = safeApiCall {
        remoteDataSource.getProductsByIds(productIds)
    }

    suspend fun addOrder (order: OrderClass){
        remoteDataSource.addOrder(order)
    }

}