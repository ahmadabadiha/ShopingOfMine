package com.example.shopingofmine.di

import com.example.shopingofmine.model.RemoteDataSource
import com.example.shopingofmine.model.Repository
import com.example.shopingofmine.model.RetrofitDataSource
import com.example.shopingofmine.model.RetrofitMethods
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