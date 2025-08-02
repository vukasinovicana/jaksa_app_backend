package com.example.jaksa_app.service

import com.example.jaksa_app.controller.ClassController
import com.example.jaksa_app.model.Class
import com.example.jaksa_app.model.ClassDto
import com.example.jaksa_app.model.ClassStatus
import com.example.jaksa_app.model.ClassRequest
import com.example.jaksa_app.model.ClassesByMonthRequest
import com.example.jaksa_app.model.toDto
import com.example.jaksa_app.repository.ClassRepository
import com.example.jaksa_app.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.YearMonth

@Service
class ClassService(
    private val repository: ClassRepository,    private val userRepository: UserRepository

) {
    @Transactional
    fun findApprovedClassesByMonth(request: ClassesByMonthRequest): List<ClassDto> {
        try {
            val yearMonth = YearMonth.of(request.year, request.month)
            val startDate = yearMonth.atDay(1)
            val endDate = yearMonth.atEndOfMonth()

            return repository.findByDateBetweenAndClassStatus(startDate, endDate, ClassStatus.APPROVED).map { it.toDto() }
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Nevalidna godina ili mesec.", e)
        }
    }

    fun createClassRequest(request: ClassRequest): ResponseEntity<String> {
        val student = userRepository.findById(request.studentId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Student nije pronađen.") }

        val newClass = Class(
            student = student,
            date = request.date,
            time_start = request.timeStart,
            duration = request.duration,
            classStatus = ClassStatus.PENDING,
            description = request.description,
            requested_by_student = request.requestedByStudent
        )

        repository.save(newClass)

        return ResponseEntity.status(HttpStatus.CREATED).body("Čas je uspešno dodat.")
    }


}