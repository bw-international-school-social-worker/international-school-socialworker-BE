package com.intworkers.application.controller

import com.intworkers.application.model.auth.User
import com.intworkers.application.model.auth.UserRoles
import com.intworkers.application.service.RoleService
import com.intworkers.application.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
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

    @PostMapping(value = ["/createnewuser"], consumes = ["application/json"], produces = ["application/json"])
    @Throws(URISyntaxException::class)
    fun addNewUser(request: HttpServletRequest, @Valid
    @RequestBody
    newuser: User): ResponseEntity<*> {
        logger.trace(request.requestURI + " accessed")

        val newRoles = ArrayList<UserRoles>()
        newRoles.add(UserRoles(newuser, roleService.findByName("user")))
        newuser.userRoles = newRoles

        val newuser = userService.save(newuser)

        // set the location header for the newly created resource - to another controller!
        val responseHeaders = HttpHeaders()
        val newRestaurantURI = ServletUriComponentsBuilder
                .fromUriString(request.serverName + ":" + request.localPort + "/users/user/{userId}")
                .buildAndExpand(newuser.userid).toUri()
        responseHeaders.location = newRestaurantURI


        return ResponseEntity<Any>(null, responseHeaders, HttpStatus.CREATED)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(RolesController::class.java)
    }

}
