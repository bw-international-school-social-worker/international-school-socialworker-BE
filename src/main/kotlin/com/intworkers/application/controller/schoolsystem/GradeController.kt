package com.intworkers.application.controller.schoolsystem

import com.intworkers.application.exception.ResourceNotFoundException
import com.intworkers.application.model.schoolsystem.Course
import com.intworkers.application.model.schoolsystem.Grade
import com.intworkers.application.service.schoolsystem.ClassService
import com.intworkers.application.service.schoolsystem.GradeService
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
@RequestMapping("/grades")
class GradeController {
    @Autowired
    private lateinit var gradeService: GradeService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var schoolAdminService: SchoolAdminService

    @ApiOperation(value = "Find all grades", response = Grade::class, responseContainer = "List")
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
        return ResponseEntity(gradeService.findAll(pageable), HttpStatus.OK)
    }

    @ApiOperation(value = "Find current School admin's grades", response = Grade::class,
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
    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @GetMapping(value = ["/mygrades"], produces = ["application/json"])
    fun findMyGrades(pageable: Pageable, authentication: Authentication): ResponseEntity<*> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        val allGrades = mutableListOf<Grade>()
        for (grade in gradeService.findAll(pageable)) {
            if (grade.school?.schoolId == schoolAdmin.school?.schoolId) {
                allGrades.add(grade)
            }
        }
        return ResponseEntity(allGrades, HttpStatus.OK)
    }

    @ApiOperation(value = "Create new Grade in current School Admin's school", response = Grade::class)
    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @PostMapping(value = ["/new"], consumes = ["application/json"], produces = ["application/json "])
    fun createNewGrade(@ApiParam(value = "New Grade to be saved", required = true)
                       @Valid @RequestBody grade: Grade, authentication: Authentication): ResponseEntity<*> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        if (schoolAdmin.school == null) throw ResourceNotFoundException("Admin is not assigned to a school")
        grade.school = schoolAdmin.school
        val savedGrade = gradeService.save(grade)
        return ResponseEntity(gradeService.findById(savedGrade.gradeId), HttpStatus.CREATED)
    }

    @ApiOperation(value = "Update an existing grade in current School Admin's school",
            response = Grade::class)
    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @PutMapping(value = ["/update/{id}"], consumes = ["application/json"], produces = ["application/json"])
    fun update(@ApiParam(value = "Grade to be updated", required = true)
               @Valid @RequestBody grade: Grade,
               authentication: Authentication,
               @ApiParam(value = "Id of the grade to be updated", required = true, example = "1")
               @PathVariable id: Long): ResponseEntity<Any> {
        val savedGrade = gradeService.findById(id)
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        return if (savedGrade.school?.schoolId == schoolAdmin.school?.schoolId) {
            ResponseEntity(gradeService.update(grade, id), HttpStatus.OK)
        } else ResponseEntity(HttpStatus.CONFLICT)
    }

    @ApiOperation(value = "Delete an existing grade in current School Admin's school",
            response = Void::class)
    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @DeleteMapping(value = ["/delete/{id}"])
    fun delete(@ApiParam(value = "Id of the grade to be deleted", required = true, example = "1")
               @PathVariable id: Long, authentication: Authentication): ResponseEntity<Any> {
        val savedGrade = gradeService.findById(id)
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        return if (savedGrade.school?.schoolId == schoolAdmin.school?.schoolId) {
            gradeService.delete(id)
            ResponseEntity(HttpStatus.OK)
        } else ResponseEntity(HttpStatus.CONFLICT)
    }
}