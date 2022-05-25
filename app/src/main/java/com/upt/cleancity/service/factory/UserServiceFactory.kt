package com.upt.cleancity.service.factory

import com.upt.cleancity.service.UserService
import com.upt.cleancity.utils.AppState
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UserServiceFactory {

    fun makeService(): UserService {
        val httpClient = OkHttpClient.Builder()

        val builder = Retrofit.Builder()
            .baseUrl(AppState.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        return builder
            .client(httpClient.build())
            .build().create(UserService::class.java)
    }

}