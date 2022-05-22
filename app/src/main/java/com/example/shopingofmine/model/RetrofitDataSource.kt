package com.example.shopingofmine.model

import com.example.shopingofmine.model.serverdataclass.ProductItem
import retrofit2.Response
import javax.inject.Inject

class RetrofitDataSource @Inject constructor(private val retrofitMethods: RetrofitMethods) : RemoteDataSource {

    override suspend fun getProducts(orderBy: String): Response<List<ProductItem>> {
        return retrofitMethods.getProducts(orderBy)
    }

}