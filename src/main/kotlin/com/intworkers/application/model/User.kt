package com.intworkers.application.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.ArrayList
import javax.persistence.*

@Table(name = "users")
class User(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val userid: Long = 0,

        @Column(nullable = false, unique = true)
        val username: String = "",

        @Column(nullable = false)
        val password: String = "") {

        @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
        @JsonIgnoreProperties("user")
        var userRoles: List<UserRoles> = ArrayList()

        val authority: List<SimpleGrantedAuthority>
                get() {
                        val rtnList = ArrayList<SimpleGrantedAuthority>()

                        for (r in this.userRoles) {
                                val myRole = "ROLE_" + r.role!!.name!!.toUpperCase()
                                rtnList.add(SimpleGrantedAuthority(myRole))
                        }

                        return rtnList
                }

        constructor() {}

        constructor(name: String, password: String, userRoles: List<UserRoles>) {
                username = name
                setPassword(password)
                for (ur in userRoles) {
                        ur.user = this
                }
                this.userRoles = userRoles
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