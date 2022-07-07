package com.example.shopingofmine.data.repository

import com.example.shopingofmine.data.model.appmodels.AppCustomer
import com.example.shopingofmine.data.model.appmodels.AppOrderClass
import com.example.shopingofmine.data.model.appmodels.AppReview
import com.example.shopingofmine.data.model.appmodels.UpdatingOrderClass
import com.example.shopingofmine.data.remote.RemoteDataSource
import com.example.shopingofmine.data.remote.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val remoteDataSource: RemoteDataSource) {
    suspend fun getProducts(orderBy: String, order: String, perPage: Int) = safeApiCall {
        remoteDataSource.getProducts(orderBy, order, perPage)
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

    suspend fun addOrder(order: AppOrderClass) = safeApiCall {
        remoteDataSource.addOrder(order)
    }

    suspend fun searchProducts(query: String, orderBy: String, order: String) = safeApiCall {
        remoteDataSource.searchProducts(query, orderBy, order)
    }

    suspend fun getProductReviews(productIds: Array<Int>, perPage: Int) = safeApiCall {
        remoteDataSource.getProductReviews(productIds.joinToString(","), perPage)
    }

    suspend fun addReview(review: AppReview) = safeApiCall {
        remoteDataSource.addReview(review)
    }

    suspend fun createCustomer(customer: AppCustomer) = safeApiCall {
        remoteDataSource.createCustomer(customer)
    }

    suspend fun getCustomer(customerId: Int) = safeApiCall {
        remoteDataSource.getCustomer(customerId)
    }

    suspend fun getCustomerOrders(customerId: Int, status: String = "pending") = safeApiCall {
        remoteDataSource.getCustomerOrders(customerId, status)
    }

    suspend fun updateOrder(orderId: Int, updatedOrder: UpdatingOrderClass) = safeApiCall {
        remoteDataSource.updateOrder(orderId, updatedOrder)
    }

    suspend fun getCoupon(code: String) = safeApiCall {
        remoteDataSource.getCoupon(code)
    }
}
