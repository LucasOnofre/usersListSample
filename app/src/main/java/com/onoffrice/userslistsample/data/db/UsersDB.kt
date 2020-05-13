package com.onoffrice.userslistsample.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.onoffrice.userslistsample.data.model.User

@Database(entities = [User::class], version = 1)
abstract class UsersDB: RoomDatabase()  {
    abstract fun usersDao(): UsersDao
}
