package com.intworkers.application.service.schoolsystem

import com.intworkers.application.exception.ResourceNotFoundException
import com.intworkers.application.model.schoolsystem.Student
import com.intworkers.application.repository.schoolsystem.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Component
@Service(value = "studentService")
class StudentServiceImpl: StudentService {

    @Autowired
    private lateinit var studentRepository: StudentRepository

    @Autowired
    private lateinit var socialWorkerRepository: SocialWorkerRepository

    @Autowired
    private lateinit var schoolRepository: SchoolRepository

    @Autowired
    private lateinit var gradeRepository: GradeRepository

    @Autowired
    private lateinit var classRepository: ClassRepository

    override fun findById(id: Long): Student {
        return studentRepository.findById(id)
                .orElseThrow{ResourceNotFoundException("Student with id $id not found")}
    }

    override fun findAll(): MutableList<Student> {
        val students = mutableListOf<Student>()
        studentRepository.findAll().iterator().forEachRemaining{students.add(it)}
        return students
    }

    @Transactional
    @Modifying
    override fun save(student: Student): Student {
        val newStudent = Student()
        newStudent.firstName = student.firstName
        newStudent.lastName = student.lastName
        newStudent.age = student.age
        newStudent.backgroundStory = student.backgroundStory
        newStudent.contactInfo = student.contactInfo
        newStudent.hasBirthCertificate = student.hasBirthCertificate
        newStudent.hasInsurance = student.hasInsurance
        newStudent.specialNeeds = student.specialNeeds
        newStudent.status = student.status
        newStudent.photoUrl = student.photoUrl
        newStudent.insuranceExpiration = student.insuranceExpiration
        newStudent.school = student.school
        newStudent.grade = student.grade
        newStudent.studentClass = student.studentClass
        newStudent.worker = student.worker
        return studentRepository.save(newStudent)
    }

    @Transactional
    @Modifying
    override fun update(student: Student, id: Long): Student {
       val updateStudent = studentRepository.findById(id)
               .orElseThrow { ResourceNotFoundException("Student with id $id not found") }
        if (student.firstName != null) updateStudent.firstName = student.firstName
        if (student.lastName != null) updateStudent.lastName = student.lastName
        if (student.age != null) updateStudent.age = student.age
        if (student.backgroundStory != null) updateStudent.backgroundStory = student.backgroundStory
        if (student.contactInfo != null) updateStudent.contactInfo = student.contactInfo
        if (student.hasBirthCertificate != null) updateStudent.hasBirthCertificate = student.hasBirthCertificate
        if (student.hasInsurance != null) updateStudent.hasInsurance = student.hasInsurance
        if (student.specialNeeds != null) updateStudent.specialNeeds = student.specialNeeds
        if (student.status != null) updateStudent.status = student.status
        if (student.photoUrl != null) updateStudent.photoUrl = student.photoUrl
        if (student.insuranceExpiration != null) updateStudent.insuranceExpiration = student.insuranceExpiration
        if (student.grade != null) updateStudent.grade = student.grade
        if (student.studentClass != null) updateStudent.studentClass = student.studentClass
        if (student.worker != null) updateStudent.worker = student.worker
        return studentRepository.save(updateStudent)
    }

    @Transactional
    @Modifying
    override fun assignToWorker(studentId: Long, student: Student) {
        val updateStudent = studentRepository.findById(studentId)
                .orElseThrow { ResourceNotFoundException("Student with id $studentId not found") }
        if (student.worker != null) updateStudent.worker = student.worker
        studentRepository.save(updateStudent)
    }

    @Transactional
    @Modifying
    override fun assignToClass(studentId: Long, student: Student) {
        val updateStudent = studentRepository.findById(studentId)
                .orElseThrow { ResourceNotFoundException("Student with id $studentId not found") }
        if (student.studentClass != null) updateStudent.studentClass = student.studentClass
        studentRepository.save(updateStudent)
    }

    @Transactional
    @Modifying
    override fun assignToGrade(studentId: Long, student: Student) {
        val updateStudent = studentRepository.findById(studentId)
                .orElseThrow { ResourceNotFoundException("Student with id $studentId not found") }
        if (student.grade != null) updateStudent.grade = student.grade
        studentRepository.save(updateStudent)
    }

    @Transactional
    @Modifying
    override fun assignToSchool(studentId: Long, student: Student) {
        val updateStudent = studentRepository.findById(studentId)
                .orElseThrow { ResourceNotFoundException("Student with id $studentId not found") }
        if (student.school != null) updateStudent.school = student.school
        studentRepository.save(updateStudent)
    }

    @Transactional
    @Modifying
    override fun delete(studentId: Long) {
        if (studentRepository.findById(studentId).isPresent)
            studentRepository.deleteById(studentId)
        else throw ResourceNotFoundException("Student with id $studentId not found")
    }

    @Transactional
    @Modifying
    override fun removeFromWorker(studentId: Long, student: Student) {
        val updateStudent = studentRepository.findById(studentId)
                .orElseThrow { ResourceNotFoundException("Student with id $studentId not found") }
        if (student.worker == null) updateStudent.worker = student.worker
        studentRepository.save(updateStudent)
    }

    override fun removeFromClass(studentId: Long, student: Student) {
        val updateStudent = studentRepository.findById(studentId)
                .orElseThrow { ResourceNotFoundException("Student with id $studentId not found") }
        if (student.studentClass == null) updateStudent.studentClass = student.studentClass
        studentRepository.save(updateStudent)
    }

    override fun removeFromGrade(studentId: Long, student: Student) {
        val updateStudent = studentRepository.findById(studentId)
                .orElseThrow { ResourceNotFoundException("Student with id $studentId not found") }
        if (student.grade == null) updateStudent.grade = student.grade
        studentRepository.save(updateStudent)
    }

    override fun removeFromSchool(studentId: Long, student: Student) {
        val updateStudent = studentRepository.findById(studentId)
                .orElseThrow { ResourceNotFoundException("Student with id $studentId not found") }
        if (student.school == null) updateStudent.school = student.school
        studentRepository.save(updateStudent)
    }
}