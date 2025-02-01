package com.shadow.deepseekimp.di

import com.shadow.deepseekimp.data.service.SnackBarServiceImpl
import com.shadow.deepseekimp.domain.utils.SnackBarService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Service {

    @Singleton
    @Provides
    fun provideSnackBarService() : SnackBarService {
        return SnackBarServiceImpl()
    }
}