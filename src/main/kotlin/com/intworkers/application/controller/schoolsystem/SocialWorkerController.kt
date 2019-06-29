package com.intworkers.application.controller.schoolsystem

import com.intworkers.application.exception.ResourceNotFoundException
import com.intworkers.application.model.user.SchoolAdmin
import com.intworkers.application.model.user.SocialWorker
import com.intworkers.application.service.OrgService
import com.intworkers.application.service.schoolsystem.SchoolAdminService
import com.intworkers.application.service.schoolsystem.SchoolService
import com.intworkers.application.service.schoolsystem.SocialWorkerService
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
import javax.validation.Valid

@RestController
@RequestMapping("/socialworkers")
class SocialWorkerController {

    @Autowired
    private lateinit var socialWorkerService: SocialWorkerService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var schoolService: SchoolService

    @Autowired
    private lateinit var schoolAdminService: SchoolAdminService

    @Autowired
    private lateinit var orgService: OrgService

    @ApiOperation(value = "Find all Social Workers", response = SocialWorker::class, responseContainer = "List")
    @ApiImplicitParams(
            ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported."))
    @PreAuthorize("hasAnyAuthority('ROLE_SCHOOLADMIN', 'ROLE_ADMIN')")
    @GetMapping(value = ["/all"], produces = ["application/json"])
    fun findAll(pageable: Pageable): ResponseEntity<*> {
        val workers = socialWorkerService.findAll(pageable)
        return ResponseEntity(workers, HttpStatus.OK)
    }

    @ApiOperation(value = "Find a Social Worker by Id", response = SocialWorker::class)
    @PreAuthorize("hasAnyAuthority('ROLE_SCHOOLADMIN', 'ROLE_ADMIN')")
    @GetMapping(value = ["/worker/{id}"], produces = ["application/json"])
    fun findById(@ApiParam(value = "Social Worker Id", required = true, example = "1")
                 @PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity(socialWorkerService.findById(id), HttpStatus.OK)
    }

    @ApiOperation(value = "Find current Social Worker's info", response = SocialWorker::class)
    @PreAuthorize("hasAuthority('ROLE_SOCIALWORKER')")
    @GetMapping(value = ["/myinfo"], produces = ["application/json"])
    fun currentWorkerInfo(authentication: Authentication): ResponseEntity<*> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        return ResponseEntity(socialWorkerService.findById(user.userId), HttpStatus.OK)
    }

    @ApiOperation(value = "Update current Social Worker's info", response = SocialWorker::class)
    @PreAuthorize("hasAuthority('ROLE_SOCIALWORKER')")
    @PutMapping(value = ["/myinfo"], consumes = ["application/json"],
            produces = ["application/json"])
    fun updateMyInfo(authentication: Authentication,
                     @ApiParam(value = "Social Worker with info to update", required = true)
                     @Valid @RequestBody workerToUpdate: SocialWorker): ResponseEntity<*> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        return ResponseEntity(socialWorkerService.update(workerToUpdate, user.userId), HttpStatus.OK)
    }

    @ApiOperation(value = "Delete current Social Worker from system", response = Void::class)
    @PreAuthorize("hasAuthority('ROLE_SOCIALWORKER')")
    @DeleteMapping(value = ["/myinfo"])
    fun deleteMyInfo(authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        userService.delete(user.userId)
        return ResponseEntity(HttpStatus.OK)
    }

    @ApiOperation(value = "Assign Social Worker to current School Admin's School", response = Void::class)
    @PreAuthorize("hasAnyAuthority('ROLE_SCHOOLADMIN', 'ROLE_ADMIN')")
    @PostMapping(value = ["/assigntoschool/{workerId}"])
    fun assignToSchool(@ApiParam(value = "Social Worker Id", required = true, example = "1")
                       @PathVariable workerId: Long,
                       authentication: Authentication
    ): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        if (schoolAdmin.school == null) throw ResourceNotFoundException("School Admin is not assigned to a school")
        socialWorkerService.assignWorkerToSchool(workerId, schoolAdmin.school!!.schoolId)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @ApiOperation(value = "Remove a Social Worker from current School Admin's school", response = Void::class)
    @PreAuthorize("hasAnyAuthority('ROLE_SCHOOLADMIN', 'ROLE_ADMIN')")
    @DeleteMapping(value = ["/removefromschool/{workerId}"])
    fun removeFromSchool(@ApiParam(value = "Social Worker Id", required = true, example = "1")
                         @PathVariable workerId: Long,
                         authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        if (schoolAdmin.school == null) throw ResourceNotFoundException("School Admin is not assigned to a school")
        socialWorkerService.removeWorkerFromSchool(workerId, schoolAdmin.school!!.schoolId)
        val worker = socialWorkerService.findById(workerId)
        worker.students.clear()
        socialWorkerService.save(worker)
        return ResponseEntity(HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_SOCIALWORKER')")
    @PutMapping(value = ["/joinorg/{id}"])
    fun joinOrg(@PathVariable id: Long, authentication: Authentication): ResponseEntity<*> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val socialworker = socialWorkerService.findById(user.userId)
        val org = orgService.findById(id)
        socialworker.organization = org
        return ResponseEntity(socialWorkerService.update(socialworker, socialworker.workerid), HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_SOCIALWORKER')")
    @DeleteMapping(value = ["/leaveorg"])
    fun leaveOrg(authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        socialWorkerService.leaveOrg(user.userId)
        return ResponseEntity(HttpStatus.OK)
    }

}