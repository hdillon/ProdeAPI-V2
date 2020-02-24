package com.solution.prode.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.ObjectMapper
import org.hibernate.annotations.NaturalId
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size


@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(columnNames = ["username"]), UniqueConstraint(columnNames = ["email"])])
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    var id: Long? = null

    @NotBlank
    @Size(max = 40)
    var name: String? = null

    @NotBlank
    @Size(max = 15)
    var username: String? = null

    @NaturalId
    @NotBlank
    @Size(max = 40)
    @Email
    var email: String? = null

    @NotBlank
    @Size(max = 100, min = 8)
    @JsonIgnore
    var password: String? = null

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Role::class)
    @JoinTable(name = "user_role", joinColumns = [JoinColumn(name = "user_id")], inverseJoinColumns = [JoinColumn(name = "role_id")])
    var roles: Set<Role> =  HashSet()
}

fun User.toJson(): String = ObjectMapper().writeValueAsString(this)