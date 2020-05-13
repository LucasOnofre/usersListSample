package com.onoffrice.userslistsample.presentation.users

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.onoffrice.userslistsample.R
import com.onoffrice.userslistsample.data.model.User
import com.onoffrice.userslistsample.data.model.UsersResult
import com.onoffrice.userslistsample.data.network.repository.UsersRepository

class UsersViewModel(private val dataSource: UsersRepository) : ViewModel() {

    val loadingEvent           : MutableLiveData<Boolean>         = MutableLiveData()
    val errorLiveData          : MutableLiveData<String>          = MutableLiveData()
    val usersListLiveData      : MutableLiveData<List<User>>      = MutableLiveData()
    val emptyListErrorLiveData : MutableLiveData<Int>             = MutableLiveData()

    fun getUsers() {
        loadingEvent.value = true

        dataSource.getUsers { result ->
            when (result) {
                is UsersResult.Success -> {
                    usersListLiveData.value = result.users
                }
                is UsersResult.EmptyListError -> {
                    emptyListErrorLiveData.value = R.string.list_empty_error
                }
                is UsersResult.ServerError -> {
                    errorLiveData.value = result.message
                }
            }
            //Only get's called when get users returns something
            loadingEvent.value = false
        }
    }

    /** Creates a factory to get the view model with a data source **/
    class ViewModelFactory(private val dataSource: UsersRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UsersViewModel::class.java)) {
                return UsersViewModel(dataSource) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}