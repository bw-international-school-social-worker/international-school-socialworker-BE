package com.intworkers.application.service

import com.example.sprint14challenge.model.User

interface UserService {

    fun findAll(): List<User>

    fun findUserById(id: Long): User

    fun delete(id: Long)

    fun save(user: User): User

    fun update(user: User, id: Long): User
}