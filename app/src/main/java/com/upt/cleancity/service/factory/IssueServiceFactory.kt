package com.upt.cleancity.service.factory

import android.content.Context
import com.upt.cleancity.service.IssueService
import com.upt.cleancity.service.common.AuthInterceptor
import com.upt.cleancity.service.common.CustomHttpClient
import com.upt.cleancity.utils.AppState
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object IssueServiceFactory {

    fun makeService(context: Context): IssueService {
        val builder = Retrofit.Builder()
            .baseUrl(AppState.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        return builder
            .client(CustomHttpClient.getCustomHttpClientBuilder(context).addInterceptor(AuthInterceptor()).build())
            .build().create(IssueService::class.java)
    }

}