package com.onoffrice.userslistsample.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.onoffrice.userslistsample.data.model.User

@Dao
abstract class UsersDao {

    @Query("SELECT * FROM users_table")
    abstract fun getAllUsers(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUsers(users: List<User>)

}