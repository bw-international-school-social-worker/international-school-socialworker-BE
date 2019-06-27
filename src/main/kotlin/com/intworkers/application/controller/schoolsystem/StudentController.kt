package com.intworkers.application.controller.schoolsystem

import com.intworkers.application.exception.ResourceNotFoundException
import com.intworkers.application.model.schoolsystem.Student
import com.intworkers.application.service.schoolsystem.*
import com.intworkers.application.service.user.UserService
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
@RequestMapping("students")
class StudentController {

    @Autowired
    private lateinit var studentService: StudentService

    @Autowired
    private lateinit var schoolAdminService: SchoolAdminService

    @Autowired
    private lateinit var socialWorkerService: SocialWorkerService

    @Autowired
    private lateinit var classService: ClassService

    @Autowired
    private lateinit var gradeService: GradeService

    @Autowired
    private lateinit var schoolService: SchoolService

    @Autowired
    private lateinit var userService: UserService

    @GetMapping(value = ["/student/{id}"], produces = ["application/json"])
    fun findById(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity(studentService.findById(id), HttpStatus.OK)
    }

    @GetMapping(value = ["/all"], produces = ["application/json"])
    fun findAll(): ResponseEntity<*> {
        return ResponseEntity(studentService.findAll(), HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_SOCIALWORKER')")
    @GetMapping(value = ["/socialworker/all"], produces = ["application/json"])
    fun findAllWorkerStudents(pageable: Pageable,
                              authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val socialWorker = socialWorkerService.findById(user.userId)
        return ResponseEntity(socialWorker.students, HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @GetMapping(value = ["/schooladmin/all"], produces = ["application/json"])
    fun findAllAdminStudents(authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        val allStudents = studentService.findAll()
        val adminStudents = mutableListOf<Student>()
        for (student in allStudents) {
            if (student.school?.schoolId == schoolAdmin.school?.schoolId)
                adminStudents.add(student)
        }
        return ResponseEntity(adminStudents, HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @PostMapping(value = ["/new"], consumes = ["application/json"], produces = ["application/json"])
    fun createNewStudent(@Valid @RequestBody student: Student,
                         authentication: Authentication): ResponseEntity<*> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        if (schoolAdmin.school == null) throw ResourceNotFoundException("Admin is not assigned to a school")
        student.school = schoolAdmin.school
        return ResponseEntity(studentService.save(student).studentId, HttpStatus.CREATED)
    }

    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @PutMapping(value = ["/update/{id}"], consumes = ["application/json"], produces = ["application/json"])
    fun updateStudent(@Valid @RequestBody student: Student, @PathVariable id: Long,
                      authentication: Authentication): ResponseEntity<*> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        val currentStudent = studentService.findById(id)
        if (currentStudent.school?.schoolId != schoolAdmin.school?.schoolId)
            throw ResourceNotFoundException("Student does not attend the current admin's school")
        return ResponseEntity(studentService.update(student, id), HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @DeleteMapping(value = ["/delete/{id}"])
    fun deleteStudent(@PathVariable id: Long, authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        val currentStudent = studentService.findById(id)
        if (currentStudent.school?.schoolId != schoolAdmin.school?.schoolId)
            throw ResourceNotFoundException("Student does not attend the current admin's school")
        studentService.delete(id)
        return ResponseEntity(HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @PostMapping(value = ["{studentId}/assigntoworker/{workerId}"])
    fun assignToWorker(@PathVariable studentId: Long,
                       @PathVariable workerId: Long,
                       authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        val currentStudent = studentService.findById(studentId)
        if (currentStudent.school?.schoolId != schoolAdmin.school?.schoolId)
            throw ResourceNotFoundException("Student does not attend the current admin's school")
        currentStudent.worker = socialWorkerService.findById(workerId)
        studentService.assignToWorker(studentId, currentStudent)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @PostMapping(value = ["{studentId}/assigntoclass/{classId}"])
    fun assignToClass(@PathVariable studentId: Long,
                      @PathVariable classId: Long,
                      authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        val currentStudent = studentService.findById(studentId)
        if (currentStudent.school?.schoolId != schoolAdmin.school?.schoolId)
            throw ResourceNotFoundException("Student does not attend the current admin's school")
        currentStudent.studentClass = classService.findById(classId)
        studentService.assignToClass(studentId, currentStudent)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @PostMapping(value = ["{studentId}/assigntograde/{gradeId}"])
    fun assignToGrade(@PathVariable studentId: Long,
                      @PathVariable gradeId: Long,
                      authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        val currentStudent = studentService.findById(studentId)
        if (currentStudent.school?.schoolId != schoolAdmin.school?.schoolId)
            throw ResourceNotFoundException("Student does not attend the current admin's school")
        currentStudent.grade = gradeService.findById(gradeId)
        studentService.assignToGrade(studentId, currentStudent)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @PostMapping(value = ["{studentId}/assigntoschool"])
    fun assignToSchool(@PathVariable studentId: Long,
                       authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        val currentStudent = studentService.findById(studentId)
        if (currentStudent.school?.schoolId != null)
            throw ResourceNotFoundException("Student currently belongs to a school")
        if (schoolAdmin.school == null)
            throw ResourceNotFoundException("Current School Admin does not have a School")
        currentStudent.school = schoolAdmin.school
        studentService.assignToSchool(studentId, currentStudent)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @DeleteMapping(value = ["{studentId}/removefromworker"])
    fun removeFromWorker(@PathVariable studentId: Long,
                         authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        val currentStudent = studentService.findById(studentId)
        if (currentStudent.school?.schoolId != schoolAdmin.school?.schoolId)
            throw ResourceNotFoundException("Student does not attend the current admin's school")
        currentStudent.worker = null
        studentService.update(currentStudent, studentId)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @DeleteMapping(value = ["{studentId}/removefromclass"])
    fun removeFromClass(@PathVariable studentId: Long,
                        authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        val currentStudent = studentService.findById(studentId)
        if (currentStudent.school?.schoolId != schoolAdmin.school?.schoolId)
            throw ResourceNotFoundException("Student does not attend the current admin's school")
        currentStudent.studentClass = null
        studentService.update(currentStudent, studentId)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @DeleteMapping(value = ["{studentId}/removefromgrade"])
    fun removeFromGrade(@PathVariable studentId: Long,
                        authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        val currentStudent = studentService.findById(studentId)
        if (currentStudent.school?.schoolId != schoolAdmin.school?.schoolId)
            throw ResourceNotFoundException("Student does not attend the current admin's school")
        currentStudent.grade = null
        studentService.update(currentStudent, studentId)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @DeleteMapping(value = ["{studentId}/removefromschool"])
    fun removeFromSchool(@PathVariable studentId: Long,
                         authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        val currentStudent = studentService.findById(studentId)
        if (currentStudent.school?.schoolId != schoolAdmin.school?.schoolId)
            throw ResourceNotFoundException("Student does not attend the current admin's school")
        currentStudent.school = null
        studentService.update(currentStudent, studentId)
        return ResponseEntity(HttpStatus.CREATED)
    }

}