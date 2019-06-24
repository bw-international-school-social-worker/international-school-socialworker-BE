package com.intworkers.application.controller

import com.intworkers.application.model.user.SchoolAdmin
import com.intworkers.application.model.user.SocialWorker
import com.intworkers.application.model.user.User
import com.intworkers.application.model.user.UserRoles
import com.intworkers.application.service.schoolsystem.SchoolAdminService
import com.intworkers.application.service.schoolsystem.SocialWorkerService
import com.intworkers.application.service.user.RoleService
import com.intworkers.application.service.user.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import java.net.URISyntaxException
import java.util.ArrayList

@Component
@RestController
class OpenController {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var socialWorkerService: SocialWorkerService

    @Autowired
    private lateinit var schoolAdminService: SchoolAdminService

    @PostMapping(value = ["/createnewuser/{role}"], consumes = ["application/json"], produces = ["application/json"])
    @Throws(URISyntaxException::class)
    fun addNewUser(request: HttpServletRequest, @Valid
    @RequestBody
    newUser: User, @PathVariable role: String): ResponseEntity<Any> {
        if (role != "socialworker" && role != "schooladmin") throw Exception()
        val newRoles = ArrayList<UserRoles>()
        newRoles.add(UserRoles(newUser, roleService.findByName(role)))
        newUser.userRoles = newRoles
        val savedUser = userService.save(newUser)
        if (role == "socialworker") {
            val socialWorker = SocialWorker()
            socialWorker.user = savedUser
            socialWorker.workerid = savedUser.userId
            return ResponseEntity(socialWorkerService.save(socialWorker), HttpStatus.CREATED)
        } else if (role == "schooladmin") {
            val schoolAdmin = SchoolAdmin()
            schoolAdmin.user = savedUser
            schoolAdmin.adminId = savedUser.userId
            return ResponseEntity(schoolAdminService.save(schoolAdmin), HttpStatus.CREATED)
        }
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(RolesController::class.java)
    }

}
