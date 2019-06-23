package com.intworkers.application.service.user

import com.intworkers.application.model.user.Role

interface RoleService {
    fun findAll(): List<Role>

    fun findRoleById(id: Long): Role

    fun delete(id: Long)

    fun save(role: Role): Role

    fun findByName(name: String): Role
}