package com.solution.prode.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.ObjectMapper
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.Table
import javax.persistence.UniqueConstraint
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size
import org.hibernate.annotations.NaturalId

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
    var roles: Set<Role> = HashSet()
}

fun User.toJson(): String = ObjectMapper().writeValueAsString(this)
