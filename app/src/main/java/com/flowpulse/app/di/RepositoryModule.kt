package com.flowpulse.app.di

import com.flowpulse.app.data.repo.FlowPulseRepositoryImpl
import com.flowpulse.app.domain.repo.FlowPulseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindFlowPulseRepository(impl: FlowPulseRepositoryImpl): FlowPulseRepository
}
