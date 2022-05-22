package com.example.shopingofmine.model

import com.example.shopingofmine.model.serverdataclass.ProductItem
import retrofit2.Response

interface RemoteDataSource {
    suspend fun getProducts(orderBy: String): Response<List<ProductItem>>
}