package com.intworkers.application.model.schoolsystem

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
@Table(name = "classes")
class Course(
        @Column
        var className: String? = null,

        @ManyToOne
        @JoinColumn(name = "schoolId")
        @JsonIgnoreProperties("classes")
        var school: School? = null,

        @OneToMany(mappedBy = "studentClass", cascade = [CascadeType.ALL],
                orphanRemoval = false)
        @JsonIgnoreProperties("class")
        var students: MutableList<Student> = mutableListOf()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var classId: Long = 0
}