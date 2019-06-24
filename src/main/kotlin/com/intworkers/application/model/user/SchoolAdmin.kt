package com.intworkers.application.model.user

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.intworkers.application.model.schoolsystem.School
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "school_admins")
class SchoolAdmin(
        @Id
        @Column(name = "adminId")
        var adminId: Long = 0,

        @OneToOne
        @JoinColumn(name = "adminId")
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

        @OneToOne(mappedBy = "schoolAdmin", cascade = [CascadeType.ALL],
                orphanRemoval = false)
        @JsonIgnoreProperties("schoolAdmin")
        var school: School? = null
): Serializable /* {
        override fun equals(o: Any?): Boolean {
                if (this === o) {
                        return true
                }
                if (o !is SchoolAdmin) {
                        return false
                }
                val schoolAdmin = o as SchoolAdmin?
                return user == schoolAdmin!!.user
        }

        override fun hashCode(): Int {
                return Objects.hash(user)
        }
} */