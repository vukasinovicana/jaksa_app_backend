package com.example.jaksa_app

import com.example.jaksa_app.model.User
import com.example.jaksa_app.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class RepositoriesTests @Autowired constructor(
        val entityManager: TestEntityManager,
        val userRepository: UserRepository
) {

    @Test
    fun `When findByLogin then return User`() {
        val johnDoe = User("johnDoe", "John", "Doe")
        entityManager.persist(johnDoe)
        entityManager.flush()
        val user = userRepository.findByLogin(johnDoe.login)
        assertThat(user).isEqualTo(johnDoe)
    }
}