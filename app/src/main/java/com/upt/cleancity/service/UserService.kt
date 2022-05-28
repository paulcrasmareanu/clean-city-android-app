package com.upt.cleancity.service

import com.upt.cleancity.model.LoginContract
import com.upt.cleancity.model.Token
import com.upt.cleancity.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {

    @POST("api/Auth/signUp")
    fun createNewUser(@Body user: User): Call<Void>

    @POST("api/Auth/login/")
    fun loginUser(@Body login: LoginContract) : Call<Token>

    @GET("api/User")
    fun getAllUsers(): Call<List<User>>

}