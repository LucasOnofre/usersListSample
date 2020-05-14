package com.onoffrice.userslistsample.android.presentation.users

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.verify
import com.onoffrice.userslistsample.R
import com.onoffrice.userslistsample.data.model.User
import com.onoffrice.userslistsample.data.model.UsersResult
import com.onoffrice.userslistsample.data.network.repository.UsersRepository
import com.onoffrice.userslistsample.presentation.users.UsersViewModel
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UsersViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var errorLiveDataObserver : Observer<String>

    @Mock
    private lateinit var emptyListErrorLiveDataObserver : Observer<Int>

    @Mock
    private lateinit var usersListLiveDataObserver : Observer<List<User>>

    private lateinit var viewModel: UsersViewModel

    @Test
    fun `when viewModel getUsers result in success then sets usersListLiveData`() {
        //Arrange
        val users = arrayListOf(
            User(id = 1, img = "Image 1", name = "name 1", username = "username 1")
        )

        val resultSuccess = MockRepository(UsersResult.Success(users))
        viewModel = UsersViewModel(resultSuccess)
        viewModel.usersListLiveData.observeForever(usersListLiveDataObserver)

        //Act
        viewModel.getUsers()

        // Assert
        verify(usersListLiveDataObserver).onChanged(users)
        Assert.assertEquals(viewModel.usersListLiveData.value, users)
    }

    @Test
    fun `when viewModel getUsers result in error then sets errorLiveData`() {
        //Arrange
        val message = "Erro"

        val resultError = MockRepository(UsersResult.ServerError(message))
        viewModel = UsersViewModel(resultError)
        viewModel.errorLiveData.observeForever(errorLiveDataObserver)

        //Act
        viewModel.getUsers()

        // Assert
        verify(errorLiveDataObserver).onChanged(message)
        Assert.assertEquals(viewModel.errorLiveData.value, message)
    }

    @Test
    fun `when viewModel getUsers result in success but list is empty then sets errorLiveData`() {
        //Arrange
        val resultEmptyList = MockRepository(UsersResult.EmptyListError)
        viewModel = UsersViewModel(resultEmptyList)
        viewModel.emptyListErrorLiveData.observeForever(emptyListErrorLiveDataObserver)

        //Act
        viewModel.getUsers()

        // Assert
        verify(emptyListErrorLiveDataObserver).onChanged(R.string.list_empty_error)
        Assert.assertEquals(viewModel.emptyListErrorLiveData.value, R.string.list_empty_error)
    }
}

class MockRepository(private val result: UsersResult) : UsersRepository {
    override fun getUsers(usersResultCallback: (result: UsersResult) -> Unit) {
        usersResultCallback(result)
    }
}