package com.onoffrice.userslistsample.data.network.service

import com.onoffrice.userslistsample.data.model.User
import retrofit2.Call
import retrofit2.http.GET

interface UserService {

    @GET("users")
    fun getUsers(): Call<List<User>>
}