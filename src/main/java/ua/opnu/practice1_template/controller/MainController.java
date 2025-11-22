package ua.opnu.practice1_template.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.opnu.practice1_template.service.*;

@Controller
public class MainController {

    private final StudentService studentService;
    private final InstructorService instructorService;
    private final DrivingGroupService drivingGroupService;
    private final LessonService lessonService;

    public MainController(StudentService studentService,
                          InstructorService instructorService,
                          DrivingGroupService drivingGroupService,
                          LessonService lessonService) {
        this.studentService = studentService;
        this.instructorService = instructorService;
        this.drivingGroupService = drivingGroupService;
        this.lessonService = lessonService;
    }

    @GetMapping("/main")
    public String main() {
        return "main";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    // Сторінка студентів з даними
    @GetMapping("/web/students")
    public String studentsPage(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "students";
    }

    // Сторінка інструкторів з даними
    @GetMapping("/web/instructors")
    public String instructorsPage(Model model) {
        model.addAttribute("instructors", instructorService.getAllInstructors());
        return "instructors";
    }

    // Сторінка груп з даними
    @GetMapping("/web/groups")
    public String groupsPage(Model model) {
        model.addAttribute("groups", drivingGroupService.getAllGroups());
        return "groups";
    }

    // Сторінка занять з даними
    @GetMapping("/web/lessons")
    public String lessonsPage(Model model) {
        model.addAttribute("lessons", lessonService.getAllLessons());
        return "lessons";
    }
}