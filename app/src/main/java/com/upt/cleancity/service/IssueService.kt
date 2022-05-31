package com.upt.cleancity.service

import com.upt.cleancity.model.Issue
import retrofit2.Call
import retrofit2.http.*

interface IssueService {

    @GET("/api/issue")
    fun getAllIssues(): Call<List<Issue>>

    @GET("/api/issue/{id}")
    fun getIssue(@Path("id") issueId: String): Call<Issue>

    @POST("/api/issue")
    fun saveIssue(@Body issue: Issue): Call<Issue>

    @DELETE("/api/issue/{id}")
    fun deleteIssue(@Path("id") issueId: String): Call<Void>

}