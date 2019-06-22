package com.intworkers.application.controller

import com.intworkers.application.model.SchoolAdmin
import com.intworkers.application.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import java.net.URISyntaxException

@Component
@RestController
@RequestMapping("/users")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @GetMapping(value = ["/admins"], produces = ["application/json"])
    fun listAllAdmins(request: HttpServletRequest): ResponseEntity<*> {
        val myAdmins = userService.findAllAdmins()
        return ResponseEntity(myAdmins, HttpStatus.OK)
    }

    @GetMapping(value = ["/admin/{userId}"], produces = ["application/json"])
    fun getAdmin(request: HttpServletRequest, @PathVariable userId: Long): ResponseEntity<*> {
        val u = userService.findAdminById(userId)
        return ResponseEntity(u, HttpStatus.OK)
    }

    @GetMapping(value = ["/getusername"], produces = ["application/json"])
    @ResponseBody
    fun getCurrentUserName(request: HttpServletRequest, authentication: Authentication): ResponseEntity<*> {
        return ResponseEntity(authentication.principal, HttpStatus.OK)
    }

    @PutMapping(value = ["/admin/{id}"])
    fun updateAdmin(request: HttpServletRequest, @RequestBody updateAdmin: SchoolAdmin,
                    @PathVariable id: Long): ResponseEntity<*> {
        userService.updateAdmin(updateAdmin, id)
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    @DeleteMapping("/admin/{id}")
    fun deleteAdminById(request: HttpServletRequest, @PathVariable id: Long): ResponseEntity<*> {
        userService.deleteAdmin(id)
        return ResponseEntity<Any>(HttpStatus.OK)
    }
}