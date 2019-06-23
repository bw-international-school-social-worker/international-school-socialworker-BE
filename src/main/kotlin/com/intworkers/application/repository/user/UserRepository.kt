package com.intworkers.application.repository.user

import com.intworkers.application.model.user.User
import org.springframework.data.repository.PagingAndSortingRepository

interface UserRepository : PagingAndSortingRepository<User, Long> {
    fun findByUsername(username: String): User
}
