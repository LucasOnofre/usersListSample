package com.onoffrice.userslistsample.data.network.repository

import com.onoffrice.userslistsample.data.model.UsersResult

interface UsersRepository {
    fun getUsers(usersResultCallback: (result: UsersResult) -> Unit)
}