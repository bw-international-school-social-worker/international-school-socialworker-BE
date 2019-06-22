package com.intworkers.application.model.schoolsystem

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.intworkers.application.model.auth.User
import javax.persistence.*

@Entity
@Table(name = "visits")
class Visit (
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var visitId: Long = 0,

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
)