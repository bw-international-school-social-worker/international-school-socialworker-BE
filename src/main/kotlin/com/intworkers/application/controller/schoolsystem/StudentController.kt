package com.intworkers.application.controller.schoolsystem

import com.intworkers.application.exception.ResourceNotFoundException
import com.intworkers.application.model.schoolsystem.Student
import com.intworkers.application.service.schoolsystem.*
import com.intworkers.application.service.user.UserService
import io.swagger.annotations.*
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

    @ApiOperation(value = "Find Student by Id", response = Student::class)
    @GetMapping(value = ["/student/{id}"], produces = ["application/json"])
    fun findById(@ApiParam(value = "Student Id", required = true, example = "1")
                 @PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity(studentService.findById(id), HttpStatus.OK)
    }

    @ApiOperation(value = "Find all Students", response = Student::class, responseContainer = "List")
    @ApiImplicitParams(
            ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported."))
    @GetMapping(value = ["/all"], produces = ["application/json"])
    fun findAll(): ResponseEntity<*> {
        return ResponseEntity(studentService.findAll(), HttpStatus.OK)
    }

    @ApiOperation(value = "Find all Students that belong to current Social Worker",
            response = Student::class, responseContainer = "List")
    @ApiImplicitParams(
            ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported."))
    @PreAuthorize("hasAuthority('ROLE_SOCIALWORKER')")
    @GetMapping(value = ["/socialworker/all"], produces = ["application/json"])
    fun findAllWorkerStudents(pageable: Pageable,
                              authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val socialWorker = socialWorkerService.findById(user.userId)
        return ResponseEntity(socialWorker.students, HttpStatus.OK)
    }

    @ApiOperation(value = "Find all Students that are enrolled in current School Admin's school",
            response = Student::class, responseContainer = "List")
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

    @ApiOperation(value = "Create a new Student in the current School Admin's School",
            response = Student::class)
    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @PostMapping(value = ["/new"], consumes = ["application/json"], produces = ["application/json"])
    fun createNewStudent(@ApiParam(value = "Student to be created", required = true)
                         @Valid @RequestBody student: Student,
                         authentication: Authentication): ResponseEntity<*> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        if (schoolAdmin.school == null) throw ResourceNotFoundException("Admin is not assigned to a school")
        student.school = schoolAdmin.school
        return ResponseEntity(studentService.save(student).studentId, HttpStatus.CREATED)
    }

    @ApiOperation(value = "Update an existing Student in the current School Admin's School",
            response = Student::class)
    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @PutMapping(value = ["/update/{id}"], consumes = ["application/json"], produces = ["application/json"])
    fun updateStudent(@ApiParam(value = "Student with info to be updated", required = true)
                      @Valid @RequestBody student: Student,
                      @ApiParam(value = "Student Id", required = true, example = "1")
                      @PathVariable id: Long,
                      authentication: Authentication): ResponseEntity<*> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        val currentStudent = studentService.findById(id)
        if (currentStudent.school?.schoolId != schoolAdmin.school?.schoolId)
            throw ResourceNotFoundException("Student does not attend the current admin's school")
        return ResponseEntity(studentService.update(student, id), HttpStatus.OK)
    }

    @ApiOperation(value = "Delete a student in the current School Admin's school", response = Void::class)
    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @DeleteMapping(value = ["/delete/{id}"])
    fun deleteStudent(@ApiParam(value = "Student Id", required = true, example = "1")
                      @PathVariable id: Long, authentication: Authentication): ResponseEntity<Any> {
        val username = (authentication.principal as UserDetails).username
        val user = userService.findByUsername(username)
        val schoolAdmin = schoolAdminService.findById(user.userId)
        val currentStudent = studentService.findById(id)
        if (currentStudent.school?.schoolId != schoolAdmin.school?.schoolId)
            throw ResourceNotFoundException("Student does not attend the current admin's school")
        studentService.delete(id)
        return ResponseEntity(HttpStatus.OK)
    }

    @ApiOperation(value = "Assign a Student to a Social Worker", response = Void::class)
    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @PostMapping(value = ["{studentId}/assigntoworker/{workerId}"])
    fun assignToWorker(@ApiParam(value = "Student Id", required = true, example = "1")
                       @PathVariable studentId: Long,
                       @ApiParam(value = "Social Worker Id", required = true, example = "1")
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

    @ApiOperation(value = "Assign a Student to a Class in current School Admin's School",
            response = Void::class)
    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @PostMapping(value = ["{studentId}/assigntoclass/{classId}"])
    fun assignToClass(@ApiParam(value = "Student Id", required = true, example = "1")
                      @PathVariable studentId: Long,
                      @ApiParam(value = "Class Id", required = true, example = "1")
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

    @ApiOperation(value = "Assign a Student to a Grade in current School Admin's School",
            response = Void::class)
    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @PostMapping(value = ["{studentId}/assigntograde/{gradeId}"])
    fun assignToGrade(@ApiParam(value = "Student Id", required = true, example = "1")
                      @PathVariable studentId: Long,
                      @ApiParam(value = "Grade Id", required = true, example = "1")
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

    @ApiOperation(value = "Assign a Student to current School Admin's School",
            response = Void::class)
    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @PostMapping(value = ["{studentId}/assigntoschool"])
    fun assignToSchool(@ApiParam(value = "Student Id", required = true, example = "1")
                       @PathVariable studentId: Long,
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

    @ApiOperation(value = "Remove a Student from a Social Worker",
            response = Void::class)
    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @DeleteMapping(value = ["{studentId}/removefromworker"])
    fun removeFromWorker(@ApiParam(value = "Student Id", required = true, example = "1")
                         @PathVariable studentId: Long,
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

    @ApiOperation(value = "Remove a Student from a Class",
            response = Void::class)
    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @DeleteMapping(value = ["{studentId}/removefromclass"])
    fun removeFromClass(@ApiParam(value = "Student Id", required = true, example = "1")
                        @PathVariable studentId: Long,
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

    @ApiOperation(value = "Remove a Student from a Grade",
            response = Void::class)
    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @DeleteMapping(value = ["{studentId}/removefromgrade"])
    fun removeFromGrade(@ApiParam(value = "Student Id", required = true, example = "1")
                        @PathVariable studentId: Long,
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

    @ApiOperation(value = "Remove a Student from a School",
            response = Void::class)
    @PreAuthorize("hasAuthority('ROLE_SCHOOLADMIN')")
    @DeleteMapping(value = ["{studentId}/removefromschool"])
    fun removeFromSchool(@ApiParam(value = "Student Id", required = true, example = "1")
                         @PathVariable studentId: Long,
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