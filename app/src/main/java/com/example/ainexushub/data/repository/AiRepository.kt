package com.example.ainexushub.data.repository

import android.content.Context
import com.example.ainexushub.data.local.FavoriteToolDao
import com.example.ainexushub.data.local.FavoriteToolEntity
import com.example.ainexushub.data.remote.AiToolDto
import com.example.ainexushub.data.remote.FileDownloadService
import com.example.ainexushub.data.remote.NewsApiService
import com.example.ainexushub.data.remote.ToolsApiService
import com.example.ainexushub.model.AiTool
import com.example.ainexushub.model.NewsArticle
import com.example.ainexushub.model.Recommendation
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okio.buffer
import okio.source
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class AiRepository(
    private val context: Context,
    private val toolsApiService: ToolsApiService,
    private val newsApiService: NewsApiService,
    private val fileDownloadService: FileDownloadService,
    private val favoriteToolDao: FavoriteToolDao,
    private val moshi: Moshi
) {

    private val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    private val displayFormatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("es", "ES"))

    private val recommendationsAdapter: JsonAdapter<List<Recommendation>> by lazy {
        val type = Types.newParameterizedType(List::class.java, Recommendation::class.java)
        moshi.adapter(type)
    }

    suspend fun fetchAiTools(): List<AiTool> = withContext(Dispatchers.IO) {
        try {
            toolsApiService.getAiTools().tools.map { it.toDomain(isFavorite = favoriteToolDao.isFavorite(it.name)) }
        } catch (error: Exception) {
            loadToolsFromAssets().map { it.copy(isFavorite = favoriteToolDao.isFavorite(it.name)) }
        }
    }

    suspend fun fetchAiNews(): List<NewsArticle> = withContext(Dispatchers.IO) {
        try {
            newsApiService.getAiNews().hits.mapNotNull { dto ->
                val url = dto.url ?: return@mapNotNull null
                val publishedAt = dto.createdAt?.let { created ->
                    runCatching { displayFormatter.format(isoFormatter.parse(created)) }.getOrDefault(created)
                } ?: ""
                NewsArticle(
                    title = dto.title ?: (dto._highlightResult?.title?.value ?: "Sin título"),
                    url = url,
                    source = dto.author ?: "HN",
                    publishedAt = publishedAt,
                    imageUrl = null
                )
            }
        } catch (error: Exception) {
            emptyList()
        }
    }

    fun observeFavorites(): Flow<List<AiTool>> =
        favoriteToolDao.getFavoriteTools().map { entities ->
            entities.map { entity ->
                AiTool(
                    name = entity.name,
                    description = entity.description,
                    url = entity.url,
                    image = entity.image,
                    isFavorite = true
                )
            }
        }

    suspend fun addFavorite(tool: AiTool) {
        favoriteToolDao.insertTool(
            FavoriteToolEntity(
                name = tool.name,
                description = tool.description,
                url = tool.url,
                image = tool.image
            )
        )
    }

    suspend fun removeFavorite(tool: AiTool) {
        favoriteToolDao.deleteTool(
            FavoriteToolEntity(
                name = tool.name,
                description = tool.description,
                url = tool.url,
                image = tool.image
            )
        )
    }

    suspend fun loadRecommendations(): List<Recommendation> = withContext(Dispatchers.IO) {
        try {
            context.assets.open("recommendations.json").source().buffer().use { source ->
                val json = source.readUtf8()
                recommendationsAdapter.fromJson(json) ?: emptyList()
            }
        } catch (io: IOException) {
            emptyList()
        }
    }

    private suspend fun loadToolsFromAssets(): List<AiTool> = withContext(Dispatchers.IO) {
        val json = context.assets.open("tools.json").use { it.readBytes().decodeToString() }
        val adapter: JsonAdapter<ToolsAsset> = moshi.adapter(ToolsAsset::class.java)
        val assets = adapter.fromJson(json)
        assets?.tools?.map { it.toDomain(isFavorite = false) } ?: emptyList()
    }

    suspend fun downloadJson(url: String): String = withContext(Dispatchers.IO) {
        fileDownloadService.downloadJson(url)
    }

    private fun AiToolDto.toDomain(isFavorite: Boolean): AiTool =
        AiTool(
            name = name,
            description = description,
            url = url,
            image = image,
            isFavorite = isFavorite
        )

    @JsonClass(generateAdapter = true)
    data class ToolsAsset(
        val tools: List<AiToolDto>
    )
}
