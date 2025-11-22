package ua.opnu.practice1_template.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.opnu.practice1_template.entity.Grade;
import ua.opnu.practice1_template.service.GradeService;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
public class GradeController {
    private final GradeService gradeService;

    @PreAuthorize("hasRole('ADMIN', 'INSTRUCTOR')")
    @PostMapping ("/grade")
    public ResponseEntity<Grade> createGrade(@RequestBody Grade grade) {
        return ResponseEntity.ok(gradeService.createGrade(grade));
    }

    //Всі мають право
    @GetMapping("/student/{studentId}")
    public List<Grade> getGradesByStudent(@PathVariable Long studentId) {
        return gradeService.getGradesByStudent(studentId);
    }

    //Всі мають право
    @GetMapping("/lesson/{lessonId}")
    public List<Grade> getGradesByLesson(@PathVariable Long lessonId) {
        return gradeService.getGradesByLesson(lessonId);
    }

    @PreAuthorize("hasRole('ADMIN', 'INSTRUCTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<Grade> updateGrade(@PathVariable Long id, @RequestBody Grade grade) {
        return ResponseEntity.ok(gradeService.updateGrade(id, grade));
    }

    //Всі мають право
    @GetMapping("/student/{studentId}/average")
    public ResponseEntity<Double> getAverageScoreByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(gradeService.getAverageScoreByStudentId(studentId));
    }

}