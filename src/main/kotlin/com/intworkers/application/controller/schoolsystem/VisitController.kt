package com.intworkers.application.controller.schoolsystem

import com.intworkers.application.exception.ResourceNotFoundException
import com.intworkers.application.model.schoolsystem.Visit
import com.intworkers.application.service.schoolsystem.SchoolAdminService
import com.intworkers.application.service.schoolsystem.SocialWorkerService
import com.intworkers.application.service.schoolsystem.VisitService
import com.intworkers.application.service.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/visits")
class VisitController {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var schoolAdminService: SchoolAdminService

    @Autowired
    private lateinit var visitService: VisitService

    @Autowired
    private lateinit var socialWorkerService: SocialWorkerService

    @GetMapping(value = ["/visit/{id}"], produces = ["application/json"])
    fun findById(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity(visitService.findById(id), HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = ["/all"], produces = ["application/json"])
    fun findAll(): ResponseEntity<*> {
        return ResponseEntity(visitService.findAll(), HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_SOCIALWORKER')")
    @GetMapping(value = ["socialworker/all"], produces = ["application/json"])
    fun findAllWorkerVisits(authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val socialWorker = socialWorkerService.findById(user.userId)
        val allVisits = visitService.findAll()
        val myVisits = mutableListOf<Visit>()
        for (visit in allVisits) {
            if (visit.workerId == socialWorker.workerid) myVisits.add(visit)
        }
        return ResponseEntity(myVisits, HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @GetMapping(value = ["schoolAdmin/all"], produces = ["application/json"])
    fun findAllAdminVisits(authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        val allVisits = visitService.findAll()
        val myVisits = mutableListOf<Visit>()
        for (visit in allVisits) {
            if (visit.schoolId == schoolAdmin.school?.schoolId) myVisits.add(visit)
        }
        return ResponseEntity(myVisits, HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @PostMapping(value = ["/new/{workerId}"],
            consumes = ["application/json"], produces = ["application/json"])
    fun visitSchool(@Valid @RequestBody visit: Visit, @PathVariable workerId: Long,
                    authentication: Authentication): ResponseEntity<*> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        visit.worker = socialWorkerService.findById(workerId)
        visit.school = schoolAdmin.school
        val savedVisit = visitService.save(visit)
        return ResponseEntity(visitService.findById(savedVisit.visitId), HttpStatus.CREATED)
    }

    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @PutMapping(value = ["/update/{visitId}"], consumes = ["application/json"],
            produces = ["application/json"])
    fun updateVisit(@Valid @RequestBody visit: Visit, @PathVariable visitId: Long,
                    authentication: Authentication): ResponseEntity<*> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        var updatedVisit = visitService.findById(visitId)
        if (updatedVisit.schoolId != schoolAdmin.school?.schoolId)
            throw ResourceNotFoundException("Visit does not belong to School Admin's school")
        return ResponseEntity(visitService.update(visit, visitId), HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @DeleteMapping(value = ["/delete/{visitId}"])
    fun deleteVisit(@PathVariable visitId: Long,
                    authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        var visit = visitService.findById(visitId)
        if (visit.schoolId != schoolAdmin.school?.schoolId)
            throw ResourceNotFoundException("Visit does not belong to School Admin's school")
        visitService.delete(visitId)
        return ResponseEntity(HttpStatus.OK)
    }

}