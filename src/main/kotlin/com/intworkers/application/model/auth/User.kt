package com.intworkers.application.model.auth

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.intworkers.application.model.School
import com.intworkers.application.model.Visit
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.ArrayList
import javax.persistence.*

@Entity
@Table(name = "users")
class User {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var userid: Long = 0

        @Column(nullable = false, unique = true)
        var username: String = ""

        @Column(nullable = false)
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private var password: String = ""

        @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
        @JsonIgnoreProperties("user")
        var userRoles: MutableList<UserRoles> = mutableListOf()

        @Column(nullable = true)
        var firstname: String? = null

        @Column(nullable = true)
        var lastname: String? = null

        @Column(nullable = true)
        var phone: String? = null

        @Column(nullable = true)
        var email: String? = null

        @Column(nullable = true)
        var photourl: String? = null

        @OneToOne(mappedBy = "schooladmin", cascade = [CascadeType.ALL],
                orphanRemoval = false)
        @JsonIgnoreProperties("schooladmin")
        var school: School? = null

        @ManyToMany
        @JoinTable(name = "workerSchools",
                joinColumns = [JoinColumn(name = "workerid")],
                inverseJoinColumns = [JoinColumn(name = "schoolid")])
        @JsonIgnoreProperties("schools")
        var schools: MutableList<School> = mutableListOf()

        @OneToMany(mappedBy = "worker", cascade = [CascadeType.ALL],
                orphanRemoval = false)
        @JsonIgnoreProperties("worker")
        var visits: MutableList<Visit> = mutableListOf()

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