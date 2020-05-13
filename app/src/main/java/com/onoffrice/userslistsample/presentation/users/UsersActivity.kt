package com.onoffrice.userslistsample.presentation.users

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onoffrice.userslistsample.R
import com.onoffrice.userslistsample.data.model.User
import com.onoffrice.userslistsample.data.network.repository.UsersApiDataSource
import com.onoffrice.userslistsample.utils.setVisible
import kotlinx.android.synthetic.main.activity_main.*

class UsersActivity : AppCompatActivity() {

    private var usersList: ArrayList<User> = arrayListOf()

    private val viewModel: UsersViewModel =
        UsersViewModel.ViewModelFactory(UsersApiDataSource(this@UsersActivity))
            .create(UsersViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkSavedInstanceState(savedInstanceState)
        setObservers()
    }

    private fun checkSavedInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val savedList = savedInstanceState.getParcelableArrayList<User>(SAVED_LIST_USER_KEY)
            savedList?.let {
                usersList = it
                setupAdapter(usersList)
            }
        } else {
            viewModel.getUsers()
        }
    }

    private fun setObservers() {
        viewModel.usersListLiveData.observe(this, Observer { usersListResponse ->
            setupAdapter(usersListResponse.toCollection(ArrayList()))
        })

        viewModel.errorLiveData.observe(this, Observer { message ->
              showError(message)

        })

        viewModel.emptyListErrorLiveData.observe(this, Observer { messageId ->
            showError(getString(messageId))

        })

        viewModel.loadingEvent.observe(this, Observer { isVisible ->
            usersListProgressBar.setVisible(isVisible)
        })
    }


    private fun setupAdapter(list: ArrayList<User>) {
        usersList = list

        with(usersRv) {
            layoutManager = LinearLayoutManager(
                this@UsersActivity,
                RecyclerView.VERTICAL,
                false
            )
            setHasFixedSize(true)

            adapter = UserListAdapter().apply {
                this.users = usersList
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this@UsersActivity, message, Toast.LENGTH_SHORT).show()
        if (usersList.isEmpty()) {
            usersRv.setVisible(false)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(SAVED_LIST_USER_KEY, usersList)
    }

    companion object {
        const val SAVED_LIST_USER_KEY = "USERS_LIST_KEY"
    }
}
