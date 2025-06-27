package com.example.jaksa_app.controller

import com.example.jaksa_app.model.Role
import com.example.jaksa_app.security.JwtUtil
import com.example.jaksa_app.model.User
import com.example.jaksa_app.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class AuthController(
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val jwtUtil: JwtUtil
) {

    data class LoginRequest(val username: String, val password: String)
    data class LoginResponse(val token: String)
    data class RegisterRequest(
        val firstname: String,
        val lastname: String,
        val email: String,
        val phone: String,
        val username: String,
        val password: String,
        val role: Role = Role.STUDENT
    )

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        val user = userRepository.findByUsername(loginRequest.username)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Neispravno korisničko ime ili lozinka. Pokušajte ponovo.")

        if (!passwordEncoder.matches(loginRequest.password, user.password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Neispravno korisničko ime ili lozinka. Pokušajte ponovo.")
        }

        val token = jwtUtil.generateToken(user.username)
        return ResponseEntity.ok(LoginResponse(token))
    }

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<String> {
        // Check if username or email already exists
        if (userRepository.findByUsername(request.username) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Korisničko ime je već zauzeto.")
        }

        if (userRepository.findByEmail(request.email) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email adresa je već zauzeta.")
        }

        // Hash the password
        val hashedPassword = passwordEncoder.encode(request.password)

        // Create and save the user
        val newUser = User(
            firstname = request.firstname,
            lastname = request.lastname,
            email = request.email,
            phone = request.phone,
            username = request.username,
            password = hashedPassword,
            role = request.role
        )

        userRepository.save(newUser)

        return ResponseEntity.status(HttpStatus.CREATED).body("Korisnik uspešno registrovan.")
    }
}