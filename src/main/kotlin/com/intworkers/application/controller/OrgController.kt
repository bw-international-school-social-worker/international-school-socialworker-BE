package com.intworkers.application.controller

import com.intworkers.application.model.schoolsystem.Organization
import com.intworkers.application.service.OrgService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/orgs")
class OrgController {

    @Autowired
    private lateinit var orgService: OrgService


    @GetMapping(value = ["/org/{id}"], produces = ["application/json"])
    fun findById(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity(orgService.findById(id), HttpStatus.OK)
    }

    @GetMapping(value = ["/all"], produces = ["application/json"])
    fun findAll(): ResponseEntity<*> {
        return ResponseEntity(orgService.findAll(), HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(value = ["/new"], consumes = ["application/json"],
            produces = ["application/json"])
    fun save(@Valid @RequestBody org: Organization): ResponseEntity<*> {
        return ResponseEntity(orgService.save(org), HttpStatus.CREATED)
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping(value = ["/update/{id}"], consumes = ["application/json"],
            produces = ["application/json"])
    fun update(@Valid @RequestBody org: Organization, @PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity(orgService.update(org, id), HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping(value = ["/delete/{id}"])
    fun delete(@PathVariable id: Long): ResponseEntity<Any> {
        orgService.delete(id)
        return ResponseEntity(HttpStatus.OK)
    }

}