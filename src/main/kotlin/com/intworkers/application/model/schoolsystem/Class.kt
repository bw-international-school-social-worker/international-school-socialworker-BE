package com.intworkers.application.model.schoolsystem

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
@Table(name = "classes")
class Class(
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    var classId: Long = 0
}