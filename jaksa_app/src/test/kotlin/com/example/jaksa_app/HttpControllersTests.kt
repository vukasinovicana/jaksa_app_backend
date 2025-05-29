package com.example.jaksa_app

import com.example.jaksa_app.model.User
import com.example.jaksa_app.repository.UserRepository
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.beans.factory.annotation.Autowired
import com.ninjasquad.springmockk.MockkBean

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@WebMvcTest
class HttpControllersTests(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var userRepository: UserRepository

    @Test
    fun `List users`() {
        val johnDoe = User("johnDoe", "John", "Doe")
        val janeDoe = User("janeDoe", "Jane", "Doe")
        every { userRepository.findAll() } returns listOf(johnDoe, janeDoe)
        mockMvc.perform(get("/api/user/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("\$.[0].login").value(johnDoe.login))
                .andExpect(jsonPath("\$.[1].login").value(janeDoe.login))
    }
}