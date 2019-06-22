package com.intworkers.application.model.schoolsystem

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
@Table(name = "grades")
class Grade(
        @Column(nullable = false)
        var gradeNumber: Int? = 0,

        @ManyToOne
        @JoinColumn(name = "schoolId")
        var school: School? = null,

        @OneToMany(mappedBy = "grade", cascade = [CascadeType.ALL],
                orphanRemoval = false)
        @JsonIgnoreProperties("grade")
        var students: MutableList<Student> = mutableListOf()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var gradeId: Long = 0
}