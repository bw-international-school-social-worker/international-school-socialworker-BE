package com.intworkers.application.model.schoolsystem

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.intworkers.application.model.user.User
import javax.persistence.*

@Entity
@Table(name = "visits")
class Visit (
        @ManyToOne
        @JoinColumn(name = "workerId")
        @JsonIgnoreProperties("visits")
        var worker: User? = null,

        @ManyToOne
        @JoinColumn(name = "schoolId")
        @JsonIgnoreProperties("visits")
        var school: School? = null,

        @Column
        var visitDate: String? = null,

        @Column
        var visitReason: String? = null,

        @Column
        var visitDescription: String? = null
) {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var visitId: Long = 0
}