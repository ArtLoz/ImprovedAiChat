package com.shadow.deepseekimp.di

import android.content.Context
import androidx.work.WorkManager
import com.shadow.deepseekimp.data.service.SnackBarServiceImpl
import com.shadow.deepseekimp.domain.utils.SnackBarService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Singleton
    @Provides
    fun provideWorkManager(
        @ApplicationContext context: Context
    ): WorkManager {
        return WorkManager.getInstance(context)
    }
}