package com.intworkers.application.controller

import com.intworkers.application.model.user.SchoolAdmin
import com.intworkers.application.model.user.User
import com.intworkers.application.repository.user.UserRepository
import com.intworkers.application.service.schoolsystem.SchoolAdminService
import com.intworkers.application.service.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping("schooladmins")
class SchoolAdminController {

    @Autowired
    private lateinit var schoolAdminService: SchoolAdminService

    @Autowired
    private lateinit var userService: UserService

    @GetMapping(value = ["/all"], produces = ["application/json"])
    fun findAll(pageable: Pageable): ResponseEntity<*> {
        val admins = schoolAdminService.findAll(pageable)
        return ResponseEntity(admins, HttpStatus.OK)
    }

    @GetMapping(value = ["/admin/{id}"], produces = ["application/json"])
    fun findById(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity(schoolAdminService.findById(id), HttpStatus.OK)
    }

    @GetMapping(value = ["/myinfo"], produces = ["application/json"])
    fun currentAdminInfo(authentication: Authentication): ResponseEntity<*> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        return ResponseEntity(schoolAdminService.findById(user.userId), HttpStatus.OK)
    }

    @PutMapping(value = ["myinfo"], consumes = ["application/json"],
            produces = ["application/json"])
    fun updateMyInfo(authentication: Authentication,
                   @Valid @RequestBody adminToUpdate: SchoolAdmin): ResponseEntity<*> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        return ResponseEntity(schoolAdminService.update(adminToUpdate, user.userId), HttpStatus.OK)
    }

    @DeleteMapping(value = ["myinfo"])
    fun deleteMyInfo(authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        userService.delete(user.userId)
        return ResponseEntity(HttpStatus.OK)
    }

}