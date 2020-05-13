package com.onoffrice.userslistsample.android

import com.onoffrice.userslistsample.data.model.User
import com.onoffrice.userslistsample.data.network.service.UserService

class ExampleService(
    private val service: UserService
) {

    fun example(): List<User> {
        val users = service.getUsers().execute()

        return users.body() ?: emptyList()
    }
}