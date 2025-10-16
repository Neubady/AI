package com.flowpulse.app.data.remote

import kotlinx.serialization.json.Json
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType.Companion.toMediaType

@Singleton
class N8nClientFactory @Inject constructor(
    private val baseOkHttpClient: OkHttpClient,
    private val json: Json
) {
    @OptIn(ExperimentalSerializationApi::class)
    fun create(baseUrl: String, authInterceptor: Interceptor, certificatePinner: CertificatePinner?): N8nService {
        val clientBuilder = baseOkHttpClient.newBuilder()
            .addInterceptor(authInterceptor)
        certificatePinner?.let { clientBuilder.certificatePinner(it) }
        val client = clientBuilder.build()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(client)
            .build()
        return retrofit.create(N8nService::class.java)
    }
}
