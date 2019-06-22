package com.intworkers.application.service

import com.intworkers.application.model.SchoolAdmin
import com.intworkers.application.model.User

interface SchoolAdminService {

    fun findAll(): List<SchoolAdmin>

    fun findUserById(id: Long): SchoolAdmin

    fun delete(id: Long)

    fun save(schoolAdmin: SchoolAdmin): SchoolAdmin

    fun update(schoolAdmin: SchoolAdmin, id: Long): SchoolAdmin
}