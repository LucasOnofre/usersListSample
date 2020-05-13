package com.onoffrice.userslistsample.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "users_table")
data class User(

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id") val id: Int,

    @SerializedName("img") val img: String,
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String

) : Parcelable