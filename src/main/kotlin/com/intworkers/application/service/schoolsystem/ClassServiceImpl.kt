package com.intworkers.application.service.schoolsystem

import com.intworkers.application.exception.ResourceNotFoundException
import com.intworkers.application.model.schoolsystem.Course
import com.intworkers.application.repository.schoolsystem.ClassRepository
import com.intworkers.application.repository.schoolsystem.SchoolRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Component
@Service(value = "classService")
class ClassServiceImpl : ClassService {

    @Autowired
    private lateinit var classRepository: ClassRepository

    @Autowired
    private lateinit var schoolRepository: SchoolRepository

    override fun findById(id: Long): Course {
        return classRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("Couldn't find class with id $id") }
    }

    override fun findAll(): MutableList<Course> {
        val classes = mutableListOf<Course>()
        classRepository.findAll().iterator().forEachRemaining { classes.add(it) }
        return classes
    }

    @Transactional
    override fun save(classToSave: Course): Course {
        val newClass = Course()
        newClass.className = classToSave.className
        newClass.school = classToSave.school
        for (student in classToSave.students) {
            newClass.students.add(student)
            student.studentClass = newClass
        }
        return classRepository.save(newClass)
    }

    @Transactional
    override fun update(classToUpdate: Course, id: Long): Course {
        val updatedClass = classRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("Couldn't find class with id $id ") }
        if (classToUpdate.className != null) updatedClass.className = classToUpdate.className
        if (classToUpdate.school != null) updatedClass.school = classToUpdate.school
        for (student in classToUpdate.students) {
            updatedClass.students.add(student)
            student.studentClass = updatedClass
        }
        return classRepository.save(updatedClass)
    }

    @Transactional
    override fun delete(id: Long) {
        if (classRepository.findById(id).isPresent)
            classRepository.deleteById(id)
        else throw ResourceNotFoundException("Couldn't find class with id $id")
    }

    @Transactional
    override fun assignToSchool(classId: Long, schoolId: Long) {
        if (classRepository.findById(classId).isPresent
                && schoolRepository.findById(schoolId).isPresent)
            classRepository.assignToSchool(classId, schoolId)
        else throw ResourceNotFoundException("Couldn't find class or School")
    }

    @Transactional
    override fun removeFromSchool(classId: Long, schoolId: Long) {
        if (classRepository.findById(classId).isPresent
                && schoolRepository.findById(schoolId).isPresent)
        classRepository.removeFromSchool(classId, schoolId)
        else throw ResourceNotFoundException("Couldn't find class or School")
    }
}