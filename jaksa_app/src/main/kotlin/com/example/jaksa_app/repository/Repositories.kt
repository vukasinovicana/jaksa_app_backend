package com.example.jaksa_app.repository

import com.example.jaksa_app.model.Class
import com.example.jaksa_app.model.ClassDto
import com.example.jaksa_app.model.ClassStatus
import com.example.jaksa_app.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun findByEmail(email: String): User?
}

interface ClassRepository : JpaRepository<Class, Long> {
    fun findByDateBetweenAndClassStatus(start: LocalDate, end: LocalDate, classStatus: ClassStatus): List<Class>
    fun findByStudentId(studentId: Long): List<Class>
}