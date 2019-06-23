package com.intworkers.application.service.schoolsystem

import com.intworkers.application.exception.ResourceNotFoundException
import com.intworkers.application.model.schoolsystem.Student
import com.intworkers.application.repository.schoolsystem.StudentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Component
@Service(value = "studentService")
class StudentServiceImpl: StudentService {

    @Autowired
    private lateinit var studentRepository: StudentRepository

    override fun findById(id: Long): Student {
        return studentRepository.findById(id)
                .orElseThrow{ResourceNotFoundException("Student with id $id not found")}
    }

    override fun findAll(): MutableList<Student> {
        val students = mutableListOf<Student>()
        studentRepository.findAll().iterator().forEachRemaining{students.add(it)}
        return students
    }

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
        return studentRepository.save(newStudent)
    }

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
        return studentRepository.save(updateStudent)
    }

    override fun assignToWorker(studentId: Long, workerId: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun assignToClass(studentId: Long, classId: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun assignToGrade(studentId: Long, gradeId: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun assignToSchool(studentId: Long, schoolId: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(studentId: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}