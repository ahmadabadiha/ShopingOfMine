package com.example.shopingofmine.model

import com.example.shopingofmine.util.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val remoteDataSource: RemoteDataSource) {
    suspend fun getProducts(orderBy: String)= safeApiCall{
        remoteDataSource.getProducts(orderBy)
    }
}