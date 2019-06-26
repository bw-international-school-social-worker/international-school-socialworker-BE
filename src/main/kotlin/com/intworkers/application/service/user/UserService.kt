package com.intworkers.application.service.user

import com.intworkers.application.model.user.User

interface UserService {

    fun findAll(): List<User>

    fun findUserById(id: Long): User

    fun delete(id: Long)

    fun save(user: User): User

    fun update(user: User, id: Long): User

    fun findByUsername(username: String): User

    fun clearAllTables()
}