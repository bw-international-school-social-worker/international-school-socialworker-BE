package com.intworkers.application.repository

import com.intworkers.application.model.auth.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {
    fun findByUsername(username: String): User
}
