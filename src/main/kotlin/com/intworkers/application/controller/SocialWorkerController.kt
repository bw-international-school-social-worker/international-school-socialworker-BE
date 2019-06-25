package com.intworkers.application.controller

import com.intworkers.application.exception.ResourceNotFoundException
import com.intworkers.application.model.user.SchoolAdmin
import com.intworkers.application.model.user.SocialWorker
import com.intworkers.application.service.schoolsystem.SchoolAdminService
import com.intworkers.application.service.schoolsystem.SchoolService
import com.intworkers.application.service.schoolsystem.SocialWorkerService
import com.intworkers.application.service.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("socialworkers")
class SocialWorkerController {

    @Autowired
    private lateinit var socialWorkerService: SocialWorkerService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var schoolService: SchoolService

    @Autowired
    private lateinit var schoolAdminService: SchoolAdminService

    @GetMapping(value = ["/all"], produces = ["application/json"])
    fun findAll(pageable: Pageable): ResponseEntity<*> {
        val workers = socialWorkerService.findAll(pageable)
        return ResponseEntity(workers, HttpStatus.OK)
    }

    @GetMapping(value = ["/worker/{id}"], produces = ["application/json"])
    fun findById(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity(socialWorkerService.findById(id), HttpStatus.OK)
    }

    @GetMapping(value = ["/myinfo"], produces = ["application/json"])
    fun currentWorkerInfo(authentication: Authentication): ResponseEntity<*> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        return ResponseEntity(socialWorkerService.findById(user.userId), HttpStatus.OK)
    }

    @PutMapping(value = ["myinfo"], consumes = ["application/json"],
            produces = ["application/json"])
    fun updateMyInfo(authentication: Authentication,
                     @Valid @RequestBody workerToUpdate: SocialWorker): ResponseEntity<*> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        return ResponseEntity(socialWorkerService.update(workerToUpdate, user.userId), HttpStatus.OK)
    }

    @DeleteMapping(value = ["myinfo"])
    fun deleteMyInfo(authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        userService.delete(user.userId)
        return ResponseEntity(HttpStatus.OK)
    }

    //TODO: Dccument in README
    @PostMapping(value = ["/assigntoschool/{workerId}"])
    fun assignToSchool(@PathVariable workerId: Long,
                       authentication: Authentication
                       ): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        if (schoolAdmin.school == null) throw ResourceNotFoundException("School Admin is not assigned to a school")
        socialWorkerService.assignWorkerToSchool(workerId, schoolAdmin.school!!.schoolId)
        return ResponseEntity(HttpStatus.CREATED)
    }

    //TODO: Dccument in README
    @DeleteMapping(value = ["removefromschool/{workerId}"])
    fun removeFromSchool(@PathVariable workerId: Long,
                         authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        if (schoolAdmin.school == null) throw ResourceNotFoundException("School Admin is not assigned to a school")
        socialWorkerService.removeWorkerFromSchool(workerId, schoolAdmin.school!!.schoolId)
        return ResponseEntity(HttpStatus.OK)
    }

}