package com.flowpulse.app.di

import android.app.Application
import androidx.room.Room
import com.flowpulse.app.data.local.FlowPulseDatabase
import com.flowpulse.app.data.local.dao.ExecutionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): FlowPulseDatabase =
        Room.databaseBuilder(application, FlowPulseDatabase::class.java, "flowpulse.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideExecutionDao(database: FlowPulseDatabase): ExecutionDao = database.executionDao()
}
