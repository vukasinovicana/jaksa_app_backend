package com.example.jaksa_app.service

import com.example.jaksa_app.model.ChangePasswordRequest
import com.example.jaksa_app.model.Role
import com.example.jaksa_app.model.UserDto
import com.example.jaksa_app.model.toDto
import com.example.jaksa_app.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserService(
    private val repository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun findAllUsers(): List<UserDto> =
        repository.findAll().map { it.toDto() }

    fun findAllStudents(): List<UserDto> =
        repository.findAll()
            .filter { it.role == Role.STUDENT }
            .map { it.toDto() }


    fun findByUsername(username: String) =
        repository.findByUsername(username)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Ovaj korisnik ne postoji.")

    fun getLoggedInUser(username: String): UserDto {
        val user = repository.findByUsername(username)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Korisnik nije pronadjen.")
        return user.toDto()
    }

    fun updateUser(username: String, request: UserDto): ResponseEntity<Any> {
        val user = repository.findByUsername(username)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Korisnik nije pronadjen.")

        user.firstname = request.firstname
        user.lastname = request.lastname
        user.email = request.email
        user.phone = request.phone

        repository.save(user)
        return ResponseEntity.ok("Korisnik uspešno izmenjen.")
    }

    fun changePassword(username: String, request: ChangePasswordRequest): ResponseEntity<Any> {
        val user = repository.findByUsername(username)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Korisnik nije pronadjen.")

        if (!passwordEncoder.matches(request.currentPassword, user.password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Stara lozinka nije tačna.")
        }

        user.password = passwordEncoder.encode(request.newPassword)
        repository.save(user)

        return ResponseEntity.ok("Lozinka uspešno promenjena.")
    }
}