package com.solution.prode.model

import org.hibernate.annotations.NaturalId
import javax.persistence.*

@Entity
@Table(name = "role")
data class Role (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    var name: RoleName? = null
)