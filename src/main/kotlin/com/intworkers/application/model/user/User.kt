package com.intworkers.application.model.user

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.intworkers.application.model.auditing.Auditable
import com.intworkers.application.model.schoolsystem.School
import com.intworkers.application.model.schoolsystem.Student
import com.intworkers.application.model.schoolsystem.Visit
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.ArrayList
import javax.persistence.*

@Entity
@Table(name = "users")
class User: Auditable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var userId: Long = 0

        @Column(nullable = false, unique = true)
        var username: String = ""

        @Column(nullable = false)
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private var password: String = ""

        @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
        @JsonIgnoreProperties("user")
        var userRoles: MutableList<UserRoles> = mutableListOf()

        @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL],
                orphanRemoval = true)
        @JsonIgnore
        var worker: SocialWorker? = null

        @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL],
                orphanRemoval = true)
        @JsonIgnore
        var admin: SchoolAdmin? = null

        constructor()

        constructor(name: String, password: String, userRoles: MutableList<UserRoles>) {
                username = name
                setPassword(password)
                for (ur in userRoles) {
                        ur.user = this
                }
                this.userRoles = userRoles
        }

        val authority: List<SimpleGrantedAuthority>
                get() {
                        val rtnList = ArrayList<SimpleGrantedAuthority>()

                        for (r in this.userRoles) {
                                val myRole = "ROLE_" + r.role?.name?.toUpperCase()
                                rtnList.add(SimpleGrantedAuthority(myRole))
                        }

                        return rtnList
                }

        fun getPassword(): String? {
                return password
        }

        fun setPassword(password: String) {
                val passwordEncoder = BCryptPasswordEncoder()
                this.password = passwordEncoder.encode(password)
        }

        fun setPasswordNoEncrypt(password: String) {
                this.password = password
        }
}