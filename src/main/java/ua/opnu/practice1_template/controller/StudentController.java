package ua.opnu.practice1_template.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.opnu.practice1_template.entity.Student;
import ua.opnu.practice1_template.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping ("/student")
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.createStudent(student));
    }

    //Всі мають право
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    //Всі мають право
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id, @RequestBody Student student) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return ResponseEntity.ok(studentService.updateStudent(id, student));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    //Всі мають право
    @GetMapping("/no-grades")
    public List<Student> getStudentsWithoutGrades() {
        return studentService.getStudentsWithoutGrades();
    }

}