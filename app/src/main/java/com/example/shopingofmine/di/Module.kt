package com.example.shopingofmine.di

import com.example.shopingofmine.data.remote.RemoteDataSource
import com.example.shopingofmine.data.repository.Repository
import com.example.shopingofmine.data.remote.RetrofitDataSource
import com.example.shopingofmine.data.remote.ShopService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun provideRetrofitDataSource(shopService: ShopService): RemoteDataSource = RetrofitDataSource(shopService)

    @Singleton
    @Provides
    fun provideRepository(remoteDataSource: RemoteDataSource): Repository = Repository(remoteDataSource)
}