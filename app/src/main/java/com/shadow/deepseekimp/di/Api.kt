package com.shadow.deepseekimp.di

import com.shadow.deepseekimp.data.network.api.ChatApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Api {

    @Provides
    @Singleton
    fun provideChatApi(
        retrofit: Retrofit
    ): ChatApi = retrofit.create(ChatApi::class.java)
}