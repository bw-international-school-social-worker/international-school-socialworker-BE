package com.intworkers.application.model.schoolsystem

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.intworkers.application.model.auth.User
import javax.persistence.*

@Entity
@Table(name = "schools")
class School(
        @Column(nullable = false)
        var schoolName: String? = null,

        @Column(nullable = true)
        var dateEstablished : String? = null,

        @OneToOne
        @JoinColumn(name = "adminId")
        var schoolAdmin: User? = null,

        @ManyToMany(mappedBy = "schools")
        @JsonIgnoreProperties("schools")
        var workers: MutableList<User> = mutableListOf(),

        @OneToMany(mappedBy = "school", cascade = [CascadeType.ALL],
                orphanRemoval = false)
        @JsonIgnoreProperties("school")
        var visits: MutableList<Visit> = mutableListOf(),

        @OneToMany(mappedBy = "school", cascade = [CascadeType.ALL],
                orphanRemoval = true)
        @JsonIgnoreProperties("school")
        var grades: MutableList<Grade> = mutableListOf(),

        @OneToMany(mappedBy = "school", cascade = [CascadeType.ALL],
                orphanRemoval = true)
        var classes: MutableList<Class> = mutableListOf(),

        @OneToMany(mappedBy = "school", cascade = [CascadeType.ALL],
                orphanRemoval = false)
        @JsonIgnoreProperties("school")
        var students: MutableList<Student> = mutableListOf()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var schoolId: Long = 0
}