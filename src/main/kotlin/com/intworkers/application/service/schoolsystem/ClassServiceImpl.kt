package com.intworkers.application.service.schoolsystem

import com.intworkers.application.exception.ResourceNotFoundException
import com.intworkers.application.model.schoolsystem.Course
import com.intworkers.application.repository.schoolsystem.ClassRepository
import com.intworkers.application.repository.schoolsystem.SchoolRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
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

    override fun findAll(pageable: Pageable): MutableList<Course> {
        val classes = mutableListOf<Course>()
        classRepository.findAll(pageable).iterator().forEachRemaining { classes.add(it) }
        return classes
    }

    @Transactional
    @Modifying
    override fun save(classToSave: Course): Course {
        val newClass = Course()
        if (classToSave.className == null)
            throw Exception()
        newClass.className = classToSave.className
        newClass.school = classToSave.school
        return classRepository.save(newClass)
    }

    @Transactional
    @Modifying
    override fun update(classToUpdate: Course, id: Long): Course {
        val updatedClass = classRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("Couldn't find class with id $id ") }
        if (classToUpdate.className != null) updatedClass.className = classToUpdate.className
        if (classToUpdate.school != null) updatedClass.school = classToUpdate.school
        return classRepository.save(updatedClass)
    }

    @Transactional
    @Modifying
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
    override fun removeFromSchool(classId: Long) {
        if (classRepository.findById(classId).isPresent)
            classRepository.removeFromSchool(classId)
        else throw ResourceNotFoundException("Couldn't find class or School")
    }
}