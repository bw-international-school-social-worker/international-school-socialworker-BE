package com.intworkers.application.repository

import com.intworkers.application.model.auth.Role
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

interface RoleRepository : CrudRepository<Role, Long> {
    @Transactional
    @Modifying
    @Query(value = "DELETE from UserRoles where user_id = :userId")
    fun deleteUserRolesByUserId(userId: Long)

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO UserRoles(user_id, role_id) values (:userId, :roleId)", nativeQuery = true)
    fun insertUserRoles(userId: Long, roleId: Long)

    fun findByNameIgnoreCase(name: String): Role
}
