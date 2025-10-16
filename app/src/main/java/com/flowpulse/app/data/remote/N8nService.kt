package com.flowpulse.app.data.remote

import com.flowpulse.app.data.remote.dto.ExecutionDto
import com.flowpulse.app.data.remote.dto.WorkflowDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface N8nService {
    @GET("rest/workflows")
    suspend fun getWorkflows(): List<WorkflowDto>

    @GET("rest/workflows/{id}")
    suspend fun getWorkflow(@Path("id") id: Long): WorkflowDto

    @POST("rest/workflows/{id}/activate")
    suspend fun toggleWorkflow(
        @Path("id") id: Long,
        @Body body: Map<String, Boolean>
    ): WorkflowDto

    @GET("rest/executions")
    suspend fun getExecutions(
        @Query("limit") limit: Int,
        @Query("lastId") lastId: Long?,
        @Query("status") status: String?
    ): List<ExecutionDto>

    @GET("rest/executions/{id}")
    suspend fun getExecution(@Path("id") id: Long): ExecutionDto
}
