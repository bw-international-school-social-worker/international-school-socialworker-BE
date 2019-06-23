package com.intworkers.application

import com.intworkers.application.model.schoolsystem.Course
import com.intworkers.application.model.schoolsystem.Student
import com.intworkers.application.model.user.Role
import com.intworkers.application.model.user.User
import com.intworkers.application.model.user.UserRoles
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
    private lateinit var studentService: StudentService

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        val r1 = Role("schooladmin")
        roleService.save(r1)
        val r2 = Role("socialworker")
        roleService.save(r2)

        val admins = mutableListOf<UserRoles>()
        admins.add(UserRoles(User(), r1))
        var admin = User("admin", "password", admins)
        admin = userService.save(admin)

        val workers = mutableListOf<UserRoles>()
        workers.add(UserRoles(User(), r2))
        var admin2 = User("worker", "password", admins)
        admin2 = userService.save(admin2)

        var student = Student()
        student.firstName = "Vivek"
        student.lastName = "V"
        student = studentService.save(student)
        studentService.assignToWorker(student.studentId, admin2.userId)

        var class1 = Course()
        class1.className = "Algebra"


    }
}