package com.solution.prode.security

import com.fasterxml.jackson.annotation.JsonIgnore
import com.solution.prode.model.Role
import com.solution.prode.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import java.util.stream.Collectors


class UserDetailsImpl(val id: Long?, val name: String?, private val username: String, @field:JsonIgnore val email: String?, @field:JsonIgnore private val password: String, private val authorities: Collection<GrantedAuthority>?) : UserDetails {

    override fun getUsername(): String {
        return username
    }

    override fun getPassword(): String {
        return password
    }

    override fun getAuthorities(): Collection<GrantedAuthority>? {
        return authorities
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as UserDetailsImpl
        return id == that.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    companion object {
        fun create(user: User?): UserDetailsImpl {
            val authorities: List<GrantedAuthority>? = user?.roles?.stream()?.map { role: Role -> SimpleGrantedAuthority(role.getName()!!.name) }?.collect(Collectors.toList())
            return UserDetailsImpl(
                    user?.id,
                    user?.name,
                    user?.username!!,
                    user?.email,
                    user?.password!!,
                    authorities
            )
        }
    }

}
