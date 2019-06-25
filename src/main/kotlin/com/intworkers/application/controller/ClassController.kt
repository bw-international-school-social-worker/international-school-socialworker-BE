package com.intworkers.application.controller

import com.intworkers.application.exception.ResourceNotFoundException
import com.intworkers.application.model.schoolsystem.Course
import com.intworkers.application.service.schoolsystem.ClassService
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
@RequestMapping("/classes")
class ClassController {

    @Autowired
    private lateinit var classService: ClassService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var schoolAdminService: SchoolAdminService

    @GetMapping(value = ["/all"], produces = ["application/json"])
    fun findAll(pageable: Pageable): ResponseEntity<*> {
        return ResponseEntity(classService.findAll(pageable), HttpStatus.OK)
    }

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

    @PostMapping(value = ["/new"], consumes = ["application/json"], produces = ["application/json "])
    fun createNewClass(@Valid @RequestBody course: Course, authentication: Authentication): ResponseEntity<*> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        if (schoolAdmin.school == null) throw ResourceNotFoundException("Admin is not assigned to a school")
        course.school = schoolAdmin.school
        val savedClass = classService.save(course)
        return ResponseEntity(classService.findById(savedClass.classId), HttpStatus.CREATED)
    }

    @PutMapping(value = ["/update/{id}"], consumes = ["application/json"], produces = ["application/json"])
    fun update(@Valid @RequestBody course: Course,
               authentication: Authentication, @PathVariable id: Long): ResponseEntity<Any> {
        val savedClass = classService.findById(id)
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        return if (savedClass.school?.schoolId == schoolAdmin.school?.schoolId) {
            ResponseEntity(classService.update(course, id), HttpStatus.OK)
        } else ResponseEntity(HttpStatus.CONFLICT)
    }

    @DeleteMapping(value = ["/delete/{id}"])
    fun delete(@PathVariable id: Long, authentication: Authentication): ResponseEntity<Any> {
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