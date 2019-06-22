package com.intworkers.application

import com.intworkers.application.model.School
import com.intworkers.application.model.auth.Role
import com.intworkers.application.model.auth.User
import com.intworkers.application.model.auth.UserRoles
import com.intworkers.application.service.RoleService
import com.intworkers.application.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Transactional
@Component
class UserSeedData : CommandLineRunner {

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var userService: UserService

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        val r1 = Role("admin")
        roleService.save(r1)

        val admins = mutableListOf<UserRoles>()
        admins.add(UserRoles(User(), r1))
        val admin = User("admin", "password", admins)
        userService.save(admin)

    }
}