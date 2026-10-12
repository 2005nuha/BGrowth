package com.example.bgrowth.data.remote

import com.example.bgrowth.data.model.ReportResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ReportApi {

    @GET("api/reports/summary/")
    suspend fun getReportSummary(
        @Query("period") period: String = "this_month"
    ): Response<ReportResponse>
}