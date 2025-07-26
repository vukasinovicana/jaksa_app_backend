package com.example.jaksa_app.controller
import com.example.jaksa_app.model.Role
import com.example.jaksa_app.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class AuthController(
    val authService: AuthService
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
        return authService.login(loginRequest)
    }

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<String> {
        return authService.register(request)
    }
}
