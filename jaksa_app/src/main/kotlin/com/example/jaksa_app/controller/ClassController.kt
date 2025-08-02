package com.example.jaksa_app.controller

import com.example.jaksa_app.model.ClassDto
import com.example.jaksa_app.model.ClassRequest
import com.example.jaksa_app.model.ClassesByMonthRequest
import com.example.jaksa_app.service.ClassService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/class")
class ClassController(private val classService: ClassService) {

    @PostMapping("/classesByMonth")
    fun findApprovedClassesByMonth(
        @RequestBody request: ClassesByMonthRequest
    ): ResponseEntity<List<ClassDto>> {
        return ResponseEntity.ok(classService.findApprovedClassesByMonth(request))
    }

    @PostMapping("/createClassRequest")
    fun createClassRequest(@RequestBody request: ClassRequest): ResponseEntity<String> {
        return classService.createClassRequest(request)
    }

}