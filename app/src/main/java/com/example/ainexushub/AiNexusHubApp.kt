package com.example.ainexushub

import android.app.Application
import androidx.room.Room
import com.example.ainexushub.data.local.AppDatabase
import com.example.ainexushub.data.repository.AiRepository
import com.example.ainexushub.data.remote.FileDownloadService
import com.example.ainexushub.data.remote.NewsApiService
import com.example.ainexushub.data.remote.ToolsApiService
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class AiNexusHubApp : Application() {

    lateinit var repository: AiRepository
        private set

    override fun onCreate() {
        super.onCreate()

        val moshi = Moshi.Builder().build()
        val okHttpClient = OkHttpClient.Builder().build()

        val toolsRetrofit = Retrofit.Builder()
            .baseUrl(TOOLS_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val newsRetrofit = Retrofit.Builder()
            .baseUrl(NEWS_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val downloadRetrofit = Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "ai_nexus_hub.db"
        ).build()

        repository = AiRepository(
            context = this,
            toolsApiService = toolsRetrofit.create(ToolsApiService::class.java),
            newsApiService = newsRetrofit.create(NewsApiService::class.java),
            fileDownloadService = downloadRetrofit.create(FileDownloadService::class.java),
            favoriteToolDao = database.favoriteToolDao(),
            moshi = moshi
        )
    }

    companion object {
        const val TOOLS_BASE_URL = "https://raw.githubusercontent.com/AI-Nexus-Hub/data/main/"
        const val NEWS_BASE_URL = "https://hn.algolia.com/api/v1/"
    }
}
