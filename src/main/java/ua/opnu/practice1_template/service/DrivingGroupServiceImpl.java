package ua.opnu.practice1_template.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.opnu.practice1_template.entity.DrivingGroup;
import ua.opnu.practice1_template.entity.Student;
import ua.opnu.practice1_template.exception.ResourceNotFoundException;
import ua.opnu.practice1_template.repository.DrivingGroupRepository;
import ua.opnu.practice1_template.repository.StudentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DrivingGroupServiceImpl implements DrivingGroupService {
    private final DrivingGroupRepository groupRepository;
    private final StudentRepository studentRepository;

    @Override
    public DrivingGroup createGroup(DrivingGroup group) {
        return groupRepository.save(group);
    }

    @Override
    public void addStudentToGroup(Long groupId, Long studentId) {
        DrivingGroup group = getGroupById(groupId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        group.getStudents().add(student);
        groupRepository.save(group);
    }


    @Override
    public List<Student> getStudentsInGroup(Long groupId) {
        DrivingGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + groupId));
        return group.getStudents();
    }

    @Override
    public DrivingGroup getGroupById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + id));
    }

    @Override
    public List<DrivingGroup> getAllGroups() {
        return (List<DrivingGroup>) groupRepository.findAll();
    }

    @Override
    public void removeStudentFromGroup(Long groupId, Long studentId) {
        DrivingGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + groupId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        group.getStudents().remove(student);
        groupRepository.save(group);
    }

    @Override
    public DrivingGroup updateGroup(Long id, DrivingGroup updatedGroup) {
        DrivingGroup existingGroup = getGroupById(id);
        existingGroup.setCode(updatedGroup.getCode());
        return groupRepository.save(existingGroup);
    }

    @Override
    public void deleteGroup(Long id) {
        DrivingGroup group = getGroupById(id);
        groupRepository.delete(group);
    }

    @Override
    public List<DrivingGroup> getGroupsByInstructor(Long instructorId) {
        return groupRepository.findByInstructorId(instructorId);
    }


    @Override
    public int countStudentsInGroup(Long groupId) {
        DrivingGroup group = getGroupById(groupId);
        return group.getStudents().size();
    }


}
