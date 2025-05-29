package com.example.jaksa_app.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil {
    private val key = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    fun generateToken(username: String): String =
        Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
            .signWith(key)
            .compact()

    fun validateToken(token: String): String? =
        try {
            Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).body.subject
        } catch (e: Exception) {
            null
        }
}