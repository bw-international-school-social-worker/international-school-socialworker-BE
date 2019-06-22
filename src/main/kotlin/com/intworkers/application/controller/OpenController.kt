package com.intworkers.application.controller

import com.intworkers.application.model.SchoolAdmin
import com.intworkers.application.model.User
import com.intworkers.application.model.UserRoles
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
    fun addNewAdmin(request: HttpServletRequest, @Valid
    @RequestBody
    newAdmin: SchoolAdmin): ResponseEntity<*> {
        logger.trace(request.requestURI + " accessed")

        val newRoles = ArrayList<UserRoles>()
        newRoles.add(UserRoles(newAdmin, roleService.findByName("schooladmin")))
        newAdmin.userRoles = newRoles

        userService.saveAdmin(newAdmin)

        return ResponseEntity<Any>(HttpStatus.CREATED)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(RolesController::class.java)
    }

}
