package com.intworkers.application.controller

import com.intworkers.application.model.user.SchoolAdmin
import com.intworkers.application.model.user.SocialWorker
import com.intworkers.application.service.schoolsystem.SchoolAdminService
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

    @GetMapping(value = ["/all"], produces = ["application/json"])
    fun findAll(pageable: Pageable): ResponseEntity<*> {
        val workers = socialWorkerService.findAll(pageable)
        return ResponseEntity(workers, HttpStatus.OK)
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

}