package ua.opnu.practice1_template.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.opnu.practice1_template.entity.DrivingGroup;
import ua.opnu.practice1_template.entity.Student;
import ua.opnu.practice1_template.service.DrivingGroupService;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class DrivingGroupController {
    private final DrivingGroupService groupService;

    @PreAuthorize("hasRole('ADMIN', 'INSTRUCTOR')")
    @PostMapping("/group")
    public ResponseEntity<DrivingGroup> createGroup(@RequestBody DrivingGroup group) {
        return ResponseEntity.ok(groupService.createGroup(group));
    }

    //Всі мають право
    @GetMapping
    public List<DrivingGroup> getAllGroups() {
        return groupService.getAllGroups();
    }

    //Всі мають право
    @GetMapping("/{id}")
    public ResponseEntity<DrivingGroup> getGroupById(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getGroupById(id));
    }

    @PreAuthorize("hasRole('ADMIN', 'INSTRUCTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<DrivingGroup> updateGroup(@PathVariable Long id, @RequestBody DrivingGroup group) {
        return ResponseEntity.ok(groupService.updateGroup(id, group));
    }

    @PreAuthorize("hasRole('ADMIN', 'INSTRUCTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN', 'INSTRUCTOR')")
    @PostMapping("/{groupId}/students/{studentId}")
    public ResponseEntity<Void> addStudentToGroup(@PathVariable Long groupId, @PathVariable Long studentId) {
        groupService.addStudentToGroup(groupId, studentId);
        return ResponseEntity.ok().build();
    }

    //Всі мають право
    @GetMapping("/{groupId}/students")
    public ResponseEntity<List<Student>> getStudentsInGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupService.getStudentsInGroup(groupId));
    }

    @PreAuthorize("hasRole('ADMIN', 'INSTRUCTOR')")
    @DeleteMapping("/{groupId}/students/{studentId}")
    public ResponseEntity<Void> removeStudentFromGroup(@PathVariable Long groupId, @PathVariable Long studentId) {
        groupService.removeStudentFromGroup(groupId, studentId);
        return ResponseEntity.noContent().build();
    }

    //Всі мають право
    @GetMapping("/instructor/{instructorId}")
    public List<DrivingGroup> getGroupsByInstructor(@PathVariable Long instructorId) {
        return groupService.getGroupsByInstructor(instructorId);
    }


    //Всі мають право
    @GetMapping("/{groupId}/students/count")
    public int countStudents(@PathVariable Long groupId) {
        return groupService.countStudentsInGroup(groupId);
    }

}