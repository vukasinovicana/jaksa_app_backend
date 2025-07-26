package com.example.jaksa_app.service

import com.example.jaksa_app.controller.AuthController
import com.example.jaksa_app.security.JwtUtil
import com.example.jaksa_app.model.User
import com.example.jaksa_app.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val jwtUtil: JwtUtil
) {

    fun login(loginRequest: AuthController.LoginRequest): ResponseEntity<Any> {
        val user = userRepository.findByUsername(loginRequest.username)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неисправно корисничко име или лозинка.")

        if (!passwordEncoder.matches(loginRequest.password, user.password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неисправно корисничко име или лозинка.")
        }

        val token = jwtUtil.generateToken(user.username)
        return ResponseEntity.ok(AuthController.LoginResponse(token))
    }

    fun register(request: AuthController.RegisterRequest): ResponseEntity<String> {
        if (userRepository.findByUsername(request.username) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Корисничко име је већ заузето.")
        }

        if (userRepository.findByEmail(request.email) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email адреса је већ заузета.")
        }

        val hashedPassword = passwordEncoder.encode(request.password)

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

        return ResponseEntity.status(HttpStatus.CREATED).body("Корисник успешно регистрован.")
    }
}
