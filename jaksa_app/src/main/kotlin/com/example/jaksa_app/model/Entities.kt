package com.example.jaksa_app.model

import jakarta.persistence.*



enum class Role { TEACHER, STUDENT }

@Entity
@Table(name = "users")
class User(
        var firstname: String,
        var lastname: String,
        @Column(unique = true)
        var email: String,
        var phone: String,
        @Column(unique = true)
        var username: String,
        var password: String,
        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        var role: Role,
        @Id @GeneratedValue var id: Long? = null)


data class UserDto(
        val firstname: String,
        val lastname: String,
        val email: String,
        val phone: String,
        val username: String,
        val role: Role? = null
)

fun User.toDto(): UserDto = UserDto(
        firstname = this.firstname,
        lastname = this.lastname,
        email = this.email,
        phone = this.phone,
        username = this.username,
        role = this.role
)


data class ChangePasswordRequest(
        val currentPassword: String,
        val newPassword: String
)