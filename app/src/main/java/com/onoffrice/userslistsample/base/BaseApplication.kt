package com.onoffrice.userslistsample.base

import android.app.Application
import androidx.room.Room
import com.onoffrice.userslistsample.data.db.UsersDB
import com.onoffrice.userslistsample.utils.Constants


class BaseApplication: Application() {

    companion object {
        var database: UsersDB? = null
    }

    override fun onCreate() {
        super.onCreate()

        database =
            Room.databaseBuilder(this, UsersDB::class.java, Constants.USERS_DB_NAME)
                .allowMainThreadQueries().build()
    }
}