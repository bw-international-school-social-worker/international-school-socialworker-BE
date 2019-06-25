package com.intworkers.application.controller

import com.intworkers.application.exception.ResourceNotFoundException
import com.intworkers.application.model.schoolsystem.Course
import com.intworkers.application.model.schoolsystem.Grade
import com.intworkers.application.service.schoolsystem.ClassService
import com.intworkers.application.service.schoolsystem.GradeService
import com.intworkers.application.service.schoolsystem.SchoolAdminService
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
@RequestMapping("/grades")
class GradeController {
    @Autowired
    private lateinit var gradeService: GradeService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var schoolAdminService: SchoolAdminService

    @GetMapping(value = ["/all"], produces = ["application/json"])
    fun findAll(pageable: Pageable): ResponseEntity<*> {
        return ResponseEntity(gradeService.findAll(pageable), HttpStatus.OK)
    }

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

    @PostMapping(value = ["/new"], consumes = ["application/json"], produces = ["application/json "])
    fun createNewGrade(@Valid @RequestBody grade: Grade, authentication: Authentication): ResponseEntity<*> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        if (schoolAdmin.school == null) throw ResourceNotFoundException("Admin is not assigned to a school")
        grade.school = schoolAdmin.school
        val savedGrade = gradeService.save(grade)
        return ResponseEntity(gradeService.findById(savedGrade.gradeId), HttpStatus.CREATED)
    }

    @PutMapping(value = ["/update/{id}"], consumes = ["application/json"], produces = ["application/json"])
    fun update(@Valid @RequestBody grade: Grade,
               authentication: Authentication, @PathVariable id: Long): ResponseEntity<Any> {
        val savedGrade = gradeService.findById(id)
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        return if (savedGrade.school?.schoolId == schoolAdmin.school?.schoolId) {
            ResponseEntity(gradeService.update(grade, id), HttpStatus.OK)
        } else ResponseEntity(HttpStatus.CONFLICT)
    }

    @DeleteMapping(value = ["/delete/{id}"])
    fun delete(@PathVariable id: Long, authentication: Authentication): ResponseEntity<Any> {
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