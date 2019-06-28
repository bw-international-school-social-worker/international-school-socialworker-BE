package com.intworkers.application.controller.schoolsystem

import com.intworkers.application.model.user.SchoolAdmin
import com.intworkers.application.model.user.User
import com.intworkers.application.repository.user.UserRepository
import com.intworkers.application.service.schoolsystem.SchoolAdminService
import com.intworkers.application.service.user.UserService
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
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

    @ApiOperation(value = "Find all School Admins", response = SchoolAdmin::class,
            responseContainer = "List")
    @ApiImplicitParams(
            ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported."))
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = ["/all"], produces = ["application/json"])
    fun findAll(pageable: Pageable): ResponseEntity<*> {
        val admins = schoolAdminService.findAll(pageable)
        return ResponseEntity(admins, HttpStatus.OK)
    }

    @ApiOperation(value = "Find a School Admin by Id", response = SchoolAdmin::class)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = ["/admin/{id}"], produces = ["application/json"])
    fun findById(@ApiParam(value = "Id of the School Admin", required = true, example = "1")
                 @PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity(schoolAdminService.findById(id), HttpStatus.OK)
    }

    @ApiOperation(value = "Find current School Admin's info", response = SchoolAdmin::class)
    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @GetMapping(value = ["/myinfo"], produces = ["application/json"])
    fun currentAdminInfo(authentication: Authentication): ResponseEntity<*> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        return ResponseEntity(schoolAdminService.findById(user.userId), HttpStatus.OK)
    }

    @ApiOperation(value = "Update current School Admin's info", response = SchoolAdmin::class)
    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @PutMapping(value = ["myinfo"], consumes = ["application/json"],
            produces = ["application/json"])
    fun updateMyInfo(authentication: Authentication,
                     @ApiParam(value = "School Admin with info to be updated", required = true)
                     @Valid @RequestBody adminToUpdate: SchoolAdmin): ResponseEntity<*> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        return ResponseEntity(schoolAdminService.update(adminToUpdate, user.userId), HttpStatus.OK)
    }

    @ApiOperation(value = "Delete current School Admin from system", response = Void::class)
    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @DeleteMapping(value = ["myinfo"])
    fun deleteMyInfo(authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        userService.delete(user.userId)
        return ResponseEntity(HttpStatus.OK)
    }

}