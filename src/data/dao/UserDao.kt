package com.codingwithjks.data.dao

import com.codingwithjks.data.model.User

interface UserDao {

    suspend fun insert(
        name:String,
        age:Int
    ):User?

    suspend fun getAllUser():List<User>?

    suspend fun getUserById(userId:Int):User?

    suspend fun deleteUserById(userId:Int):Int?

    suspend fun updateUser(userId: Int,name:String,age: Int):Int
}