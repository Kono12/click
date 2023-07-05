package com.kono_click.android.click.di

import android.content.Context
import com.kono_click.android.click.data.repository.BaseRepository
import com.kono_click.android.click.data.sharedPref.SharedPrefImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun provideBaseRepository(
        sharedPrefImpl: SharedPrefImpl
    ): BaseRepository = BaseRepository(sharedPrefImpl)


    @Provides
    @Singleton
    fun provideSharedPrefImpl(@ApplicationContext appContext: Context): SharedPrefImpl {
        return SharedPrefImpl(appContext)
    }
}