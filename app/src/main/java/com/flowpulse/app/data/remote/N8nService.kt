package com.flowpulse.app.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface N8nService {
    @GET("/rest/workflows")
    suspend fun getWorkflows(): WorkflowsResponse

    @GET("/rest/workflows/{id}")
    suspend fun getWorkflow(@Path("id") id: Long): WorkflowDto

    @POST("/rest/workflows/{id}/activate")
    suspend fun toggleWorkflow(
        @Path("id") id: Long,
        @Body request: ToggleWorkflowRequest
    ): WorkflowDto

    @GET("/rest/executions")
    suspend fun getExecutions(
        @Query("limit") limit: Int? = null,
        @Query("lastId") lastId: Long? = null,
        @Query("status") status: String? = null
    ): ExecutionsResponse

    @GET("/rest/executions/{id}")
    suspend fun getExecution(@Path("id") id: Long): ExecutionDto

    @DELETE("/rest/executions/{id}")
    suspend fun deleteExecution(@Path("id") id: Long)

    @GET("/rest/health")
    suspend fun getHealth(): HealthDto
}
