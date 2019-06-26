package com.intworkers.application.model.schoolsystem

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
@Table(name = "classes")
class Course(
        @Column(nullable = false)
        var className: String? = null,

        @ManyToOne
        @JoinColumn(name = "schoolId")
        @JsonIgnore
        var school: School? = null,

        @Column(name = "schoolId", insertable = false, updatable = false)
        var schoolId: Long? = 0,

        @OneToMany(mappedBy = "studentClass", cascade = [CascadeType.ALL],
                orphanRemoval = false)
        @JsonIgnoreProperties("class")
        var students: MutableList<Student> = mutableListOf()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var classId: Long = 0
}