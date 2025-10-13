package com.example.ainexushub.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ToolsResponse(
    @Json(name = "tools") val tools: List<AiToolDto>
)

@JsonClass(generateAdapter = true)
data class AiToolDto(
    val name: String,
    val description: String,
    val url: String,
    val image: String
)

@JsonClass(generateAdapter = true)
data class NewsResponse(
    val hits: List<NewsArticleDto> = emptyList()
)

@JsonClass(generateAdapter = true)
data class NewsArticleDto(
    val title: String?,
    val url: String?,
    val author: String?,
    @Json(name = "created_at") val createdAt: String?,
    val story_text: String? = null,
    val _highlightResult: HighlightResultDto? = null
)

@JsonClass(generateAdapter = true)
data class HighlightResultDto(
    val title: HighlightValueDto? = null
)

@JsonClass(generateAdapter = true)
data class HighlightValueDto(
    val value: String? = null
)
