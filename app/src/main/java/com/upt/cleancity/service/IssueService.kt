package com.upt.cleancity.service

import com.upt.cleancity.model.Issue
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface IssueService {

    //todo add endpoints to access API
    @GET("/api/issue")
    fun getAllIssues(): Call<List<Issue>>

    @GET("/api/issue/{id}")
    fun getIssue(@Path("id") issueId: String): Call<Issue>

}