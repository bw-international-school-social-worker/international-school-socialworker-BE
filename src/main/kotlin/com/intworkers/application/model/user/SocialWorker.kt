package com.intworkers.application.model.user

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.intworkers.application.model.schoolsystem.Organization
import com.intworkers.application.model.schoolsystem.School
import com.intworkers.application.model.schoolsystem.Student
import com.intworkers.application.model.schoolsystem.Visit
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "social_workers")
class SocialWorker(

        @Id
        @Column(name = "workerId")
        var workerid: Long = 0,

        @OneToOne
        @JoinColumn(name = "workerId")
        @JsonIgnore
        var user: User? = null,

        @Column(nullable = true)
        var firstName: String? = null,

        @Column(nullable = true)
        var lastName: String? = null,

        @Column(nullable = true)
        var phone: String? = null,

        @Column(nullable = true)
        var email: String? = null,

        @Column(nullable = true)
        var photoUrl: String? = null,

        @ManyToMany
        @JoinTable(name = "workerSchools",
                joinColumns = [JoinColumn(name = "workerId")],
                inverseJoinColumns = [JoinColumn(name = "schoolId")])
        @JsonIgnoreProperties("workers")
        var schools: MutableList<School> = mutableListOf(),

        @OneToMany(mappedBy = "worker", cascade = [CascadeType.ALL],
                orphanRemoval = false)
        @JsonIgnoreProperties("worker")
        var visits: MutableList<Visit> = mutableListOf(),

        @OneToMany(mappedBy = "worker", cascade = [CascadeType.ALL],
                orphanRemoval = false)
        @JsonIgnoreProperties("worker")
        var students: MutableList<Student> = mutableListOf(),

        @ManyToOne
        @JoinColumn(name = "organizationId")
        @JsonIgnoreProperties("workers")
        var organization: Organization? = null
): Serializable /* {
        override fun equals(o: Any?): Boolean {
                if (this === o) {
                        return true
                }
                if (o !is SocialWorker) {
                        return false
                }
                val socialWorker = o as SocialWorker?
                return user == socialWorker!!.user
        }

        override fun hashCode(): Int {
                return Objects.hash(user)
        }
} */