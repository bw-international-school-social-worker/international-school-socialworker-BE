package com.intworkers.application.controller.auth

import com.intworkers.application.model.user.User
import com.intworkers.application.service.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = ["/users"], produces = ["application/json"])
    fun listAllUsers(request: HttpServletRequest): ResponseEntity<*> {
        val myUsers = userService.findAll()
        return ResponseEntity(myUsers, HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = ["/user/{userId}"], produces = ["application/json"])
    fun getUser(request: HttpServletRequest, @PathVariable userId: Long?): ResponseEntity<*> {
        val u = userService.findUserById(userId!!)
        return ResponseEntity(u, HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = ["/getusername"], produces = ["application/json"])
    @ResponseBody
    fun getCurrentUserName(request: HttpServletRequest, authentication: Authentication): ResponseEntity<*> {
        return ResponseEntity(authentication.principal, HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(value = ["/user"], consumes = ["application/json"], produces = ["application/json"])
    @Throws(URISyntaxException::class)
    fun addNewUser(request: HttpServletRequest, @Valid @RequestBody newuser: User): ResponseEntity<*> {
        val newuser = userService.save(newuser)
        // set the location header for the newly created resource
        val responseHeaders = HttpHeaders()
        val newUserURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{userId}")
                .buildAndExpand(newuser.userId)
                .toUri()
        responseHeaders.location = newUserURI

        return ResponseEntity<Any>(null, responseHeaders, HttpStatus.CREATED)
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping(value = ["/user/{id}"])
    fun updateUser(request: HttpServletRequest, @RequestBody updateUser: User, @PathVariable id: Long): ResponseEntity<*> {
        userService.update(updateUser, id)
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    @PutMapping(value = ["/myinfo"],
            consumes = ["application/json"], produces = ["application/json"])
    fun updateMyInfo(@Valid @RequestBody user: User, authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val updatedUser = userService.findByUsername(username)
        userService.update(user, updatedUser.userId)
        return ResponseEntity(HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/user/{id}")
    fun deleteUserById(request: HttpServletRequest, @PathVariable id: Long): ResponseEntity<*> {
        userService.delete(id)
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping(value = ["/cleartables"])
    fun deleteAll(): ResponseEntity<Any> {
        userService.clearAllTables()
        return ResponseEntity(HttpStatus.OK)
    }
}