package com.intworkers.application.model.schoolsystem

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.intworkers.application.model.auditing.Auditable
import javax.persistence.*

@Entity
@Table(name = "grades")
class Grade(
        @Column(nullable = false)
        var gradeNumber: Int? = null,

        @ManyToOne
        @JoinColumn(name = "schoolId")
        @JsonIgnore
        var school: School? = null,

        @Column(name = "schoolId", insertable = false, updatable = false)
        var schoolId: Long? = 0,

        @OneToMany(mappedBy = "grade", cascade = [CascadeType.ALL],
                orphanRemoval = false)
        @JsonIgnoreProperties("grade")
        var students: MutableList<Student> = mutableListOf()
): Auditable() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var gradeId: Long = 0
}