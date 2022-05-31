package com.upt.cleancity.service.factory

import android.content.Context
import com.upt.cleancity.service.UserService
import com.upt.cleancity.service.common.CustomHttpClient
import com.upt.cleancity.utils.AppState
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UserServiceFactory {

    fun makeService(context: Context): UserService {
        val builder = Retrofit.Builder()
            .baseUrl(AppState.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        return builder
            .client(CustomHttpClient.getCustomHttpClientBuilder(context).build())
            .build().create(UserService::class.java)
    }

}