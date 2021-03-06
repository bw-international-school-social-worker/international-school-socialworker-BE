package com.intworkers.application.controller.auth

import com.intworkers.application.model.user.SchoolAdmin
import com.intworkers.application.model.user.SocialWorker
import com.intworkers.application.model.user.User
import com.intworkers.application.model.user.UserRoles
import com.intworkers.application.service.schoolsystem.SchoolAdminService
import com.intworkers.application.service.schoolsystem.SocialWorkerService
import com.intworkers.application.service.user.RoleService
import com.intworkers.application.service.user.UserService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

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

    @ApiOperation(value = "Create a new user with role socialworker or schooladmin")
    @ApiResponses(value = [
    ApiResponse(code = 201, message = "New School Admin successfully created", response = SchoolAdmin::class),
    ApiResponse(code = 201, message = "New Social Worker successfully created", response = SocialWorker::class),
    ApiResponse(code = 400, message = "Error creating new user", response = Void::class)])
    @PostMapping(value = ["/createnewuser/{role}"], consumes = ["application/json"], produces = ["application/json"])
    @Throws(URISyntaxException::class)
    fun addNewUser(request: HttpServletRequest, @Valid
    @RequestBody
    newUser: User, @ApiParam(value = "User role", required = true, example = "socialworker")
                   @PathVariable role: String): ResponseEntity<Any> {
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
