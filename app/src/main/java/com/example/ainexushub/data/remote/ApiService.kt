package com.example.ainexushub.data.remote

import retrofit2.http.GET
import retrofit2.http.Url

interface ToolsApiService {
    @GET("tools.json")
    suspend fun getAiTools(): ToolsResponse
}

interface NewsApiService {
    @GET("search_by_date?query=artificial%20intelligence&tags=story&hitsPerPage=20")
    suspend fun getAiNews(): NewsResponse
}

interface FileDownloadService {
    @GET
    suspend fun downloadJson(@Url url: String): String
}
