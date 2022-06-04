package com.example.shopingofmine.data.di

import com.example.shopingofmine.data.remote.RemoteDataSource
import com.example.shopingofmine.data.remote.repository.Repository
import com.example.shopingofmine.data.remote.RetrofitDataSource
import com.example.shopingofmine.data.remote.RetrofitMethods
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
    fun provideRetrofitDataSource(retrofitMethods: RetrofitMethods): RemoteDataSource = RetrofitDataSource(retrofitMethods)

    @Singleton
    @Provides
    fun provideRepository(remoteDataSource: RemoteDataSource): Repository = Repository(remoteDataSource)
}