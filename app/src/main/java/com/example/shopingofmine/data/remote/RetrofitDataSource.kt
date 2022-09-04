package com.example.shopingofmine.data.remote

import com.example.shopingofmine.data.model.apimodels.*
import com.example.shopingofmine.data.model.appmodels.AppCustomer
import com.example.shopingofmine.data.model.appmodels.AppOrderClass
import com.example.shopingofmine.data.model.appmodels.AppReview
import com.example.shopingofmine.data.model.appmodels.UpdatingOrderClass
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitDataSource @Inject constructor(private val shopService: ShopService) : RemoteDataSource {
    override suspend fun getProducts(orderBy: String, order: String, perPage: Int): Response<List<ProductItem>> {
        return shopService.getProducts(orderBy, order, perPage)
    }

    override suspend fun getCategories(): Response<List<CategoryItem>> {
        return shopService.getCategories()
    }

    override suspend fun getProductsByCategory(categoryId: String, orderBy: String, order: String): Response<List<ProductItem>> {
        return shopService.getProductsByCategory(categoryId, orderBy, order)

    }

    override suspend fun getProductsByIds(productIds: String): Response<List<ProductItem>> {
        return shopService.getProductsByIds(productIds)
    }

    override suspend fun addOrder(order: AppOrderClass): Response<Order> {
        return shopService.addOrder(order)
    }

    override suspend fun searchProducts(query: String, orderBy: String, order: String): Response<List<ProductItem>> {
        return shopService.searchProducts(query, orderBy, order)
    }

    override suspend fun getProductReviews(productIds: String, perPage: Int): Response<List<Review>> {
        return shopService.getProductsReviews(productIds, perPage)
    }

    override suspend fun addReview(review: AppReview): Response<Any> {
        return shopService.addReview(review)
    }

    override suspend fun createCustomer(customer: AppCustomer): Response<Customer> {
        return shopService.createCustomer(customer)
    }

    override suspend fun getCustomer(id: Int): Response<Customer> {
        return shopService.getCustomer(id)
    }

    override suspend fun getCustomerOrders(customerId: Int, status: String): Response<List<Order>> {
        return shopService.getCustomerOrders(customerId, status)
    }

    override suspend fun updateOrder(orderId: Int, updatedOrder: UpdatingOrderClass): Response<Order> {
        return shopService.updateOrder(orderId, updatedOrder)
    }

    override suspend fun getCoupon(code: String): Response<List<Coupon>> {
        return shopService.getCoupon(code)
    }

    override suspend fun findCustomer(email: String): Response<List<Customer>> {
        return shopService.findCustomer(email)
    }
}
