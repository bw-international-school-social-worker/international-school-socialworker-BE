package com.intworkers.application.service.schoolsystem

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
        return studentRepository.findById(id).orElseThrow()
    }

    override fun findAll(): MutableList<Student> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun save(student: Student): Student {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(student: Student, id: Long): Student {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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