package com.example.jaksa_app.controller

import com.example.jaksa_app.model.ClassDto
import com.example.jaksa_app.model.ClassRequest
import com.example.jaksa_app.model.ClassStatus
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

    @GetMapping("/allClassesForStudent/{studentId}")
    fun getAllClassesForStudent(@PathVariable studentId: Long): ResponseEntity<List<ClassDto>> {
        return ResponseEntity.ok(classService.getAllClassesForStudent(studentId))
    }

    @GetMapping("/allClasses")
    fun getAllClasses(): ResponseEntity<List<ClassDto>> {
        return ResponseEntity.ok(classService.getAllClasses())
    }

    @PutMapping("/acceptRequest/{classId}")
    fun acceptRequest(@PathVariable classId: Long): ResponseEntity<String> {
        return ResponseEntity.ok(classService.updateClassStatus(classId, ClassStatus.APPROVED))
    }

    @PutMapping("/rejectRequest/{classId}")
    fun rejectRequest(@PathVariable classId: Long): ResponseEntity<String> {
        return ResponseEntity.ok(classService.updateClassStatus(classId, ClassStatus.REJECTED))
    }

}