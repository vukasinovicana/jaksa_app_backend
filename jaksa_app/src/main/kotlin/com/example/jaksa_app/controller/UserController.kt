package com.example.jaksa_app.controller

import com.example.jaksa_app.model.ChangePasswordRequest
import com.example.jaksa_app.model.UserDto
import com.example.jaksa_app.repository.UserRepository
import com.example.jaksa_app.model.toDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.security.Principal


@RestController
@RequestMapping("/api/user")
class UserController(private val repository: UserRepository, private val passwordEncoder: PasswordEncoder,) {

    @GetMapping("/")
    fun findAll() = repository.findAll()

    @GetMapping("/allUsers")
    fun findAllUsers(): ResponseEntity<List<UserDto>> {
        val users = repository.findAll().map { it.toDto() }
        return ResponseEntity.ok(users)
    }



    @GetMapping("/{username}")
    fun findOne(@PathVariable username: String) =
            repository.findByUsername(username) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Ovaj korisnik ne postoji.")

    @GetMapping("/me")
    fun getLoggedInUser(principal: Principal): ResponseEntity<UserDto> {
        val username = principal.name
        val user = repository.findByUsername(username)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Korisnik nije pronadjen.")

        return ResponseEntity.ok(user.toDto())
    }

    @PutMapping("/me")
    fun updateUser(
        @AuthenticationPrincipal username: String, // get current user from security context
        @RequestBody request: UserDto
    ): ResponseEntity<Any> {
        val user = repository.findByUsername(username)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Korisnik nije pronadjen.")

        // Update fields if provided
        request.firstname.let { user.firstname = it }
        request.lastname.let { user.lastname = it }
        request.email.let { user.email = it }
        request.phone.let { user.phone = it }

        repository.save(user)

        return ResponseEntity.ok("Korisnik uspešno izmenjen.")
    }

    @PutMapping("/change-password")
    fun changePassword(
        @AuthenticationPrincipal username: String,
        @RequestBody request: ChangePasswordRequest
    ): ResponseEntity<Any> {
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