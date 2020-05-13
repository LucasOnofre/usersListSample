package com.onoffrice.userslistsample.data.model

sealed class UsersResult {
    class Success(val users: List<User>) : UsersResult()
    class ServerError (val message: String? = null) : UsersResult()
    object EmptyListError : UsersResult()
}