package com.intworkers.application.controller.auth

import com.intworkers.application.model.user.Role
import com.intworkers.application.service.user.RoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import java.net.URISyntaxException

@Component
@RestController
@RequestMapping("/roles")
class RolesController {

    @Autowired
    private lateinit var roleService: RoleService

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = ["/roles"], produces = ["application/json"])
    fun listRoles(request: HttpServletRequest): ResponseEntity<*> {

        val allRoles = roleService.findAll()
        return ResponseEntity(allRoles, HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = ["/role/{roleId}"], produces = ["application/json"])
    fun getRole(request: HttpServletRequest, @PathVariable roleId: Long?): ResponseEntity<*> {
        val r = roleService.findRoleById(roleId!!)
        return ResponseEntity(r, HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(value = ["/role"])
    @Throws(URISyntaxException::class)
    fun addNewRole(request: HttpServletRequest, @Valid @RequestBody newRole: Role): ResponseEntity<*> {

        val newRole = roleService.save(newRole)

        // set the location header for the newly created resource
        val responseHeaders = HttpHeaders()
        val newRoleURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{roleId}")
                .buildAndExpand(newRole.roleId)
                .toUri()
        responseHeaders.location = newRoleURI

        return ResponseEntity<Any>(null, responseHeaders, HttpStatus.CREATED)
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/role/{id}")
    fun deleteRoleById(request: HttpServletRequest, @PathVariable id: Long): ResponseEntity<*> {
        roleService.delete(id)
        return ResponseEntity<Any>(HttpStatus.OK)
    }

}
