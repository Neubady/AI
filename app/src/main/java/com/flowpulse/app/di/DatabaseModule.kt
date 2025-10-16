package com.flowpulse.app.di

import android.content.Context
import androidx.room.Room
import com.flowpulse.app.data.local.FlowPulseDatabase
import com.flowpulse.app.data.local.RoomTypeConverters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        json: Json
    ): FlowPulseDatabase {
        return Room.databaseBuilder(context, FlowPulseDatabase::class.java, "flowpulse.db")
            .addTypeConverter(RoomTypeConverters(json))
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideInstanceDao(database: FlowPulseDatabase) = database.instanceDao()

    @Provides
    fun provideWorkflowDao(database: FlowPulseDatabase) = database.workflowDao()

    @Provides
    fun provideExecutionDao(database: FlowPulseDatabase) = database.executionDao()

    @Provides
    fun provideAlertDao(database: FlowPulseDatabase) = database.alertDao()

    @Provides
    fun provideQuickActionDao(database: FlowPulseDatabase) = database.quickActionDao()
}
