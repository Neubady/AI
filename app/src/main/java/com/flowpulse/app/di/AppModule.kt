package com.flowpulse.app.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.flowpulse.app.data.repo.impl.AppStateRepositoryImpl
import com.flowpulse.app.data.repo.impl.BillingRepositoryImpl
import com.flowpulse.app.domain.repo.AppStateRepository
import com.flowpulse.app.domain.repo.BillingRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.flowPulseDataStore: DataStore<Preferences> by preferencesDataStore(name = "flowpulse_preferences")

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindAppStateRepository(impl: AppStateRepositoryImpl): AppStateRepository

    @Binds
    @Singleton
    abstract fun bindBillingRepository(impl: BillingRepositoryImpl): BillingRepository

    companion object {
        @Provides
        @Singleton
        fun provideDataStore(application: Application): DataStore<Preferences> = application.flowPulseDataStore

        @Provides
        fun provideContext(application: Application): Context = application
    }
}
