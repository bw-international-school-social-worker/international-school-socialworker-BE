package com.intworkers.application.controller.schoolsystem

import com.intworkers.application.model.schoolsystem.School
import com.intworkers.application.service.schoolsystem.SchoolAdminService
import com.intworkers.application.service.schoolsystem.SchoolService
import com.intworkers.application.service.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/schools")
class SchoolController {

    @Autowired
    private lateinit var schoolService: SchoolService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var schoolAdminService: SchoolAdminService

    @PreAuthorize("hasAnyAuthority('ROLE_SOCIALWORKER', 'ROLE_ADMIN')")
    @GetMapping(value = ["/school/{id}"], produces = ["application/json"])
    fun findById(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity(schoolService.findById(id), HttpStatus.OK)
    }

    @PreAuthorize("hasAnyAuthority('ROLE_SOCIALWORKER', 'ROLE_ADMIN')")
    @GetMapping(value = ["/all"], produces = ["application/json"])
    fun findAll(pageable: Pageable): ResponseEntity<*> {
        return ResponseEntity(schoolService.findAll(pageable), HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @GetMapping(value = ["/myschool"], produces = ["application/json"])
    fun getMySchool(authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        return if (schoolAdmin.school != null) {
            ResponseEntity(schoolService.findById(schoolAdmin.school!!.schoolId), HttpStatus.CREATED)
        } else {
            ResponseEntity(HttpStatus.CONFLICT)
        }
    }

    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @PostMapping(value = ["/myschool"], consumes = ["application/json"], produces = ["application/json"])
    fun create(@Valid @RequestBody school: School, authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        school.schoolAdmin = schoolAdmin
        val savedSchool = schoolService.save(school)
        return ResponseEntity(schoolService.findById(savedSchool.schoolId), HttpStatus.CREATED)
    }

    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @PutMapping(value = ["/myschool"], consumes = ["application/json"], produces = ["application/json"])
    fun update(@Valid @RequestBody school: School,
               authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        return if (schoolAdmin.school != null)
            ResponseEntity(schoolService.update(school, schoolAdmin.school!!.schoolId), HttpStatus.OK)
        else ResponseEntity(HttpStatus.CONFLICT)
    }

    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @DeleteMapping(value = ["/myschool"])
    fun delete(authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        return if (schoolAdmin.school != null) {
            schoolService.delete(schoolAdmin.school!!.schoolId)
            ResponseEntity(HttpStatus.OK)
        } else ResponseEntity(HttpStatus.CONFLICT)
    }
}