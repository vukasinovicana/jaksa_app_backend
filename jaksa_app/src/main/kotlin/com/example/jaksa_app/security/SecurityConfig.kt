package com.example.jaksa_app.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(val jwtUtil: JwtUtil) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun corsConfigurationSource(): UrlBasedCorsConfigurationSource {
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.allowedOrigins = listOf("http://localhost:5173")
        config.allowedHeaders = listOf("*")
        config.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }

    @Bean
    fun filterChain(http: HttpSecurity, jwtAuthenticationFilter: JwtAuthenticationFilter): SecurityFilterChain {
        http
            .cors { it.configurationSource(corsConfigurationSource()) }
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/api/login", "/api/register").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthenticationFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}


