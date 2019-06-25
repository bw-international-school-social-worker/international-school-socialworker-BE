package com.intworkers.application.model.schoolsystem

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.intworkers.application.model.user.SocialWorker
import com.intworkers.application.model.user.User
import javax.persistence.*

@Entity
@Table(name = "students")
class Student(
        @Column
        var firstName: String? = null,

        @Column
        var lastName: String? = null,

        @Column
        var photoUrl: String? = null,

        @Column
        var backgroundStory: String? = null,

        @Column
        var status: String? = null,

        @Column
        var age: Int? = null,

        @Column
        var hasInsurance: Boolean? = null,

        @Column
        var insuranceExpiration: String? = null,

        @Column
        var hasBirthCertificate: Boolean? = null,

        @Column
        var specialNeeds: String? = null,

        @Column
        var contactInfo: String? = null,

        @ManyToOne
        @JoinColumn(name = "gradeId")
        @JsonIgnore
        var grade: Grade? = null,

        @Column(name = "gradeId", insertable = false, updatable = false)
        var gradeId: Long = 0,

        @ManyToOne
        @JoinColumn(name = "classId")
        @JsonIgnore
        var studentClass: Course? = null,

        @Column(name = "classId", insertable = false, updatable = false)
        var classId: Long = 0,

        @ManyToOne
        @JoinColumn(name = "schoolId")
        @JsonIgnore
        var school: School? = null,

        @Column(name = "schoolId", insertable = false, updatable = false)
        var schoolId: Long = 0,

        @ManyToOne
        @JoinColumn(name = "workerId", referencedColumnName = "workerId")
        @JsonIgnore
        var worker: SocialWorker? = null,

        @Column(name = "workerId", insertable = false, updatable = false)
        var workerId: Long = 0
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var studentId: Long = 0
}