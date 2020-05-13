package com.onoffrice.userslistsample.data.network.repository

import android.content.Context
import com.onoffrice.userslistsample.base.BaseApplication
import com.onoffrice.userslistsample.data.model.User
import com.onoffrice.userslistsample.data.model.UsersResult
import com.onoffrice.userslistsample.data.network.service.ApiService
import com.onoffrice.userslistsample.data.network.service.UserService
import com.onoffrice.userslistsample.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersApiDataSource(context: Context) : UsersRepository {
    private val service = ApiService.createService(
        context, Constants.BASE_URL, UserService::class.java)


    /** Implements User repository, in this case, get's the users form an API **/
    override fun getUsers(usersResultCallback: (result: UsersResult) -> Unit) {
        service.getUsers().enqueue(object : Callback<List<User>> {
               override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                   when {
                       response.isSuccessful -> {
                           val users = response.body() ?: listOf()
                           checkUsersList(users, usersResultCallback)
                       }
                   }
               }

               override fun onFailure(call: Call<List<User>>, t: Throwable) {
                   usersResultCallback(UsersResult.ServerError(t.message))
               }
           })

        //Check's cached data after req was maded
        checkCache(usersResultCallback)
    }

    /** Get's room database and check if has a cached user list**/
    private fun checkCache(usersResultCallback: (result: UsersResult) -> Unit) {
        checkUsersList(
            BaseApplication.database?.usersDao()?.getAllUsers() ?: listOf(),
            usersResultCallback)
    }

    /** Check's if the users response is an empty list **/
    private fun checkUsersList(users: List<User>, usersResultCallback: (result: UsersResult) -> Unit) {
        if (users.isNotEmpty()) {
            usersResultCallback(UsersResult.Success(users))
            persistFetchedUsersList(users)
        } else {
            usersResultCallback(UsersResult.EmptyListError)
        }
    }
    /** Save the given list on the room database**/
    private fun persistFetchedUsersList(users: List<User>) {
       BaseApplication.database?.usersDao()?.insertUsers(users)
    }
}