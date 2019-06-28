package com.intworkers.application.controller.schoolsystem

import com.intworkers.application.exception.ResourceNotFoundException
import com.intworkers.application.model.schoolsystem.Course
import com.intworkers.application.service.schoolsystem.ClassService
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
import javax.validation.Valid

@RestController
@RequestMapping("/classes")
class ClassController {

    @Autowired
    private lateinit var classService: ClassService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var schoolAdminService: SchoolAdminService

    @ApiOperation(value = "Find all classes", response = Course::class, responseContainer = "List")
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
        return ResponseEntity(classService.findAll(pageable), HttpStatus.OK)
    }

    @ApiOperation(value = "Find all classes in School Admin's school",
            response = Course::class, responseContainer = "List")
    @ApiImplicitParams(
            ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported."))
    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @GetMapping(value = ["/myclasses"], produces = ["application/json"])
    fun findMyClasses(pageable: Pageable, authentication: Authentication): ResponseEntity<*> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        val allClasses = mutableListOf<Course>()
        for (course in classService.findAll(pageable)) {
            if (course.school?.schoolId == schoolAdmin.school?.schoolId) {
                allClasses.add(course)
            }
        }
        return ResponseEntity(allClasses, HttpStatus.OK)
    }

    @ApiOperation(value = "Create a new class in current School Admin's school",
            response = Course::class)
    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @PostMapping(value = ["/new"], consumes = ["application/json"], produces = ["application/json "])
    fun createNewClass(@ApiParam(value = "New Class to be created", required = true)
                       @Valid @RequestBody course: Course,
                       authentication: Authentication): ResponseEntity<*> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        if (schoolAdmin.school == null) throw ResourceNotFoundException("Admin is not assigned to a school")
        course.school = schoolAdmin.school
        return ResponseEntity(classService.save(course), HttpStatus.CREATED)
    }

    @ApiOperation(value = "Update an existing class in current School Admin's school",
            response = Course::class)
    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @PutMapping(value = ["/update/{id}"], consumes = ["application/json"], produces = ["application/json"])
    fun update(@ApiParam(value = "Class to be updated", required = true)
               @Valid @RequestBody course: Course,
               authentication: Authentication,
               @ApiParam(value = "Id of the class to be updated", required = true, example = "1")
               @PathVariable id: Long): ResponseEntity<Any> {
        val savedClass = classService.findById(id)
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        return if (savedClass.school?.schoolId == schoolAdmin.school?.schoolId) {
            ResponseEntity(classService.update(course, id), HttpStatus.OK)
        } else ResponseEntity(HttpStatus.CONFLICT)
    }

    @ApiOperation(value = "Delete an existing class in current School Admin's school",
            response = Void::class)
    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @DeleteMapping(value = ["/delete/{id}"])
    fun delete(@ApiParam(value = "Id of the class to be deleted", required = true, example = "1")
               @PathVariable id: Long, authentication: Authentication): ResponseEntity<Any> {
        val savedClass = classService.findById(id)
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        return if (savedClass.school?.schoolId == schoolAdmin.school?.schoolId) {
            classService.delete(id)
            ResponseEntity(HttpStatus.OK)
        } else ResponseEntity(HttpStatus.CONFLICT)
    }
}