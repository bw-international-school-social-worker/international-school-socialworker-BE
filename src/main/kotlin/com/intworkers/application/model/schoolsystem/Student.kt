package com.intworkers.application.model.schoolsystem

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
        @JsonIgnoreProperties("students")
        var grade: Grade? = null,

        @ManyToOne
        @JoinColumn(name = "classId")
        @JsonIgnoreProperties("students")
        var studentClass: Course? = null,

        @ManyToOne
        @JoinColumn(name = "schoolId")
        @JsonIgnoreProperties("students")
        var school: School? = null,

        @ManyToOne
        @JoinColumn(name = "workerId", referencedColumnName = "workerId")
        @JsonIgnoreProperties("students")
        var worker: SocialWorker? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var studentId: Long = 0
}