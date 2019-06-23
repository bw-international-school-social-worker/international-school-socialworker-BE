package com.intworkers.application.controller

import com.intworkers.application.model.user.User
import com.intworkers.application.model.user.UserRoles
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

    @PostMapping(value = ["/createnewuser/{role}"], consumes = ["application/json"], produces = ["application/json"])
    @Throws(URISyntaxException::class)
    fun addNewSchoolAdmin(request: HttpServletRequest, @Valid
    @RequestBody
    schoolAdmin: User, @PathVariable role: String): ResponseEntity<*> {
        if (role != "socialworker" && role != "schooladmin") throw Exception()
        val newRoles = ArrayList<UserRoles>()
        newRoles.add(UserRoles(schoolAdmin, roleService.findByName(role)))
        schoolAdmin.userRoles = newRoles
        val newSchoolAdmin = userService.save(schoolAdmin)

        // set the location header for the newly created resource - to another controller!
        val responseHeaders = HttpHeaders()
        val newRestaurantURI = ServletUriComponentsBuilder
                .fromUriString(request.serverName + ":" + request.localPort + "/users/user/{userId}")
                .buildAndExpand(newSchoolAdmin.userId).toUri()
        responseHeaders.location = newRestaurantURI


        return ResponseEntity<Any>(null, responseHeaders, HttpStatus.CREATED)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(RolesController::class.java)
    }

}
