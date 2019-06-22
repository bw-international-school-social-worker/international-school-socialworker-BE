package com.intworkers.application.repository

import com.intworkers.application.model.SchoolAdmin
import com.intworkers.application.model.User
import org.springframework.data.repository.CrudRepository

interface SchoolAdminRepository : CrudRepository<SchoolAdmin, Long> {
    fun findByUsername(username: String): SchoolAdmin
}
