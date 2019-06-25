package com.intworkers.application

import com.intworkers.application.model.schoolsystem.Course
import com.intworkers.application.model.schoolsystem.Student
import com.intworkers.application.model.user.*
import com.intworkers.application.service.schoolsystem.ClassService
import com.intworkers.application.service.schoolsystem.SchoolAdminService
import com.intworkers.application.service.schoolsystem.SocialWorkerService
import com.intworkers.application.service.schoolsystem.StudentService
import com.intworkers.application.service.user.RoleService
import com.intworkers.application.service.user.UserService
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

    @Autowired
    private lateinit var socialWorkerService: SocialWorkerService

    @Autowired
    private lateinit var schoolAdminService: SchoolAdminService

    @Autowired
    private lateinit var classService: ClassService

    @Autowired
    private lateinit var studentService: StudentService

    @Throws(Exception::class)
    override fun run(vararg args: String) {
         val r1 = Role("schooladmin")
        roleService.save(r1)
        val r2 = Role("socialworker")
        roleService.save(r2)
        val r3 = Role("admin")
        roleService.save(r3)

        val role = Role()
        role.roleId = 3
        val admins = mutableListOf<UserRoles>()
        admins.add(UserRoles(User(), role))
        var user3 = User("admin", "password", admins)
        user3 = userService.save(user3)

         val schooladmins = mutableListOf<UserRoles>()
        schooladmins.add(UserRoles(User(), r1))
        var user1 = User("schooladmin", "password", schooladmins)
        user1 = userService.save(user1)
        val schoolAdmin = SchoolAdmin()
        schoolAdmin.user = user1
        schoolAdmin.adminId = user1.userId
        schoolAdminService.save(schoolAdmin)

        val workers = mutableListOf<UserRoles>()
        workers.add(UserRoles(User(), r2))
        var user2 = User("socialworker", "password", workers)
        user2 = userService.save(user2)
        val socialWorker = SocialWorker()
        socialWorker.user = user2
        socialWorker.workerid = user2.userId
        socialWorkerService.save(socialWorker)

    }
}