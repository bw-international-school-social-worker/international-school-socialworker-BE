package com.intworkers.application.repository.schoolsystem

import com.intworkers.application.model.schoolsystem.Student
import org.springframework.data.repository.PagingAndSortingRepository

interface StudentRepository: PagingAndSortingRepository<Student, Long> {

}