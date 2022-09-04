package com.example.shopingofmine.data.remote

import com.example.shopingofmine.data.model.apimodels.*
import com.example.shopingofmine.data.model.appmodels.AppCustomer
import com.example.shopingofmine.data.model.appmodels.AppOrderClass
import com.example.shopingofmine.data.model.appmodels.AppReview
import com.example.shopingofmine.data.model.appmodels.UpdatingOrderClass
import retrofit2.Response

interface RemoteDataSource {
    suspend fun getProducts(orderBy: String, order:String, perPage: Int): Response<List<ProductItem>>
    suspend fun getCategories(): Response<List<CategoryItem>>
    suspend fun getProductsByCategory(categoryId: String, orderBy: String, order: String): Response<List<ProductItem>>
    suspend fun getProductsByIds(productIds: String): Response<List<ProductItem>>
    suspend fun addOrder(order: AppOrderClass): Response<Order>
    suspend fun searchProducts(query: String, orderBy: String, order: String): Response<List<ProductItem>>
    suspend fun getProductReviews(productIds: String, perPage: Int): Response<List<Review>>
    suspend fun addReview(review: AppReview): Response<Any>
    suspend fun createCustomer(customer: AppCustomer): Response<Customer>
    suspend fun getCustomer(id: Int): Response<Customer>
    suspend fun getCustomerOrders(customerId: Int, status:String): Response<List<Order>>
    suspend fun updateOrder(orderId : Int, updatedOrder: UpdatingOrderClass): Response<Order>
    suspend fun getCoupon(code: String): Response<List<Coupon>>
    suspend fun findCustomer (email: String): Response<List<Customer>>
}