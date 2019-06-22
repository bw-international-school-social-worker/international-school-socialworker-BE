package com.intworkers.application.service

import com.intworkers.application.model.SchoolAdmin
import com.intworkers.application.model.User

interface UserService {

    fun findAllAdmins(): List<SchoolAdmin>

    fun findAdminById(id: Long): SchoolAdmin

    fun deleteAdmin(id: Long)

    fun saveAdmin(schoolAdmin: SchoolAdmin): SchoolAdmin

    fun updateAdmin(schoolAdmin: SchoolAdmin, id: Long): SchoolAdmin
}