package com.example.shopingofmine.data.remote

import com.example.shopingofmine.data.model.localdataclass.LocalOrderClass
import com.example.shopingofmine.data.model.apimodels.CategoryItem
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.data.model.apimodels.Review
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitDataSource @Inject constructor(private val retrofitService: RetrofitService) : RemoteDataSource {

    override suspend fun getProducts(orderBy: String): Response<List<ProductItem>> {
        return retrofitService.getProducts(orderBy)
    }

    override suspend fun getCategories(): Response<List<CategoryItem>> {
        return retrofitService.getCategories()
    }

    override suspend fun getProductsByCategory(categoryId: String, orderBy: String, order: String): Response<List<ProductItem>> {
        return retrofitService.getProductsByCategory(categoryId, orderBy, order)

    }

    override suspend fun getProductsByIds(productIds: String): Response<List<ProductItem>> {
        return retrofitService.getProductsByIds(productIds)
    }

    override suspend fun addOrder(order: LocalOrderClass) {
        retrofitService.addOrder(order)
    }

    override suspend fun searchProducts(query: String, orderBy: String, order: String): Response<List<ProductItem>> {
        return retrofitService.searchProducts(query, orderBy, order)
    }

    override suspend fun getProductReviews(productIds: String, perPage: Int): Response<List<Review>> {
        return retrofitService.getProductsReviews(productIds, perPage)
    }
}