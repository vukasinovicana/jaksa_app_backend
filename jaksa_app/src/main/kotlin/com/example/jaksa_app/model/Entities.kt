package com.example.jaksa_app.model

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime
import kotlin.Long


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
        val role: Role? = null,
        val id: Long? = null
)

fun User.toDto(): UserDto = UserDto(
        firstname = this.firstname,
        lastname = this.lastname,
        email = this.email,
        phone = this.phone,
        username = this.username,
        role = this.role,
        id = this.id
)

data class ChangePasswordRequest(
        val currentPassword: String,
        val newPassword: String
)

data class ClassesByMonthRequest(val year: Int, val month: Int)

enum class ClassStatus {
        APPROVED,
        REJECTED,
        PENDING
}

data class ClassDto(
        val id:Long? = null,
        val studentId: Long,
        val studentFirstName: String,
        val studentLastName: String,
        var date: LocalDate,
        var timeStart: LocalTime,
        var duration: String,
        var classStatus: ClassStatus,
        var description: String,
        val requestedByStudent: Boolean,
)

data class ClassRequest(
        val studentId: Long,
        val date: LocalDate,
        val timeStart: LocalTime,
        val duration: String,
        val description: String,
        val requestedByStudent: Boolean,
)

@Entity
@Table(name = "classes")
class Class(
        @Id @GeneratedValue var id: Long? = null,
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "student_id", referencedColumnName = "id")
        var student: User,
        var date: LocalDate,
        var time_start: LocalTime,
        var duration: String,
        @Enumerated(EnumType.STRING)
        var classStatus: ClassStatus,
        var description: String,
        var requested_by_student:Boolean,
)

fun Class.toDto(): ClassDto = ClassDto(
        id = this.id,
        studentId = this.student.id!!,
        studentFirstName = this.student.firstname,
        studentLastName = this.student.lastname,
        date = this.date,
        timeStart = this.time_start,
        duration = this.duration,
        classStatus=this.classStatus,
        description = this.description,
        requestedByStudent =this.requested_by_student
)
