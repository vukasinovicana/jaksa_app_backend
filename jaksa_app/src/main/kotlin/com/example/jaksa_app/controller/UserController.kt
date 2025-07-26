package com.example.jaksa_app.controller

import com.example.jaksa_app.model.ChangePasswordRequest
import com.example.jaksa_app.model.UserDto
import com.example.jaksa_app.repository.UserRepository
import com.example.jaksa_app.model.toDto
import com.example.jaksa_app.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.security.Principal


@RestController
@RequestMapping("/api/user")
class UserController(private val userService: UserService) {

    @GetMapping("/allUsers")
    fun findAllUsers(): ResponseEntity<List<UserDto>> {
        return ResponseEntity.ok(userService.findAllUsers())
    }

    @GetMapping("/{username}")
    fun findOne(@PathVariable username: String) = userService.findByUsername(username)

    @GetMapping("/me")
    fun getLoggedInUser(principal: Principal): ResponseEntity<UserDto> {
        return ResponseEntity.ok(userService.getLoggedInUser(principal.name))
    }

    @PutMapping("/me")
    fun updateUser(
        @AuthenticationPrincipal username: String,
        @RequestBody request: UserDto
    ): ResponseEntity<Any> {
        return userService.updateUser(username, request)
    }

    @PutMapping("/change-password")
    fun changePassword(
        @AuthenticationPrincipal username: String,
        @RequestBody request: ChangePasswordRequest
    ): ResponseEntity<Any> {
        return userService.changePassword(username, request)
    }
}