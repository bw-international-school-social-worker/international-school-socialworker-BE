package com.intworkers.application.model.schoolsystem

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.intworkers.application.model.user.SchoolAdmin
import com.intworkers.application.model.user.SocialWorker
import com.intworkers.application.model.user.User
import javax.persistence.*

@Entity
@Table(name = "schools")
class School(
        @Column(nullable = false)
        var schoolName: String? = null,

        @Column(nullable = true)
        var dateEstablished : String? = null,

        @OneToOne
        @JoinColumn(name = "adminId", referencedColumnName = "adminId")
        @JsonIgnoreProperties("school")
        var schoolAdmin: SchoolAdmin? = null,

        @Column(name = "adminId", insertable = false, updatable = false)
        var adminId: Long? = 0,

        @ManyToMany(mappedBy = "schools")
        @JsonIgnore
        var workers: MutableList<SocialWorker> = mutableListOf(),

        @OneToMany(mappedBy = "school", cascade = [CascadeType.ALL],
                orphanRemoval = false)
        @JsonIgnore
        var visits: MutableList<Visit> = mutableListOf(),

        @OneToMany(mappedBy = "school", cascade = [CascadeType.ALL],
                orphanRemoval = true)
        @JsonIgnore
        var grades: MutableList<Grade> = mutableListOf(),

        @OneToMany(mappedBy = "school", cascade = [CascadeType.ALL],
                orphanRemoval = true)
        @JsonIgnore
        var classes: MutableList<Course> = mutableListOf(),

        @OneToMany(mappedBy = "school", cascade = [CascadeType.ALL],
                orphanRemoval = false)
        @JsonIgnore
        var students: MutableList<Student> = mutableListOf()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var schoolId: Long = 0
}