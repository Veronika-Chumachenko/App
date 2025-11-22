package ua.opnu.practice1_template.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.opnu.practice1_template.entity.Grade;
import ua.opnu.practice1_template.entity.Lesson;
import ua.opnu.practice1_template.entity.Student;
import ua.opnu.practice1_template.exception.ResourceNotFoundException;
import ua.opnu.practice1_template.repository.GradeRepository;
import ua.opnu.practice1_template.repository.LessonRepository;
import ua.opnu.practice1_template.repository.StudentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {
    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;


    @Override
    public Grade createGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    @Override
    public List<Grade> getGradesByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        return gradeRepository.findByStudent(student);
    }

    @Override
    public List<Grade> getGradesByLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + lessonId));
        return gradeRepository.findByLesson(lesson);
    }

    @Override
    public Grade updateGrade(Long id, Grade updatedGrade) {
        Grade existingGrade = gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade not found with id: " + id));
        existingGrade.setScore(updatedGrade.getScore());
        return gradeRepository.save(existingGrade);
    }


    @Override
    public Double getAverageScoreByStudentId(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        List<Grade> grades = gradeRepository.findByStudent(student);
        if (grades.isEmpty()) {
            return 0.0;
        }

        double total = grades.stream()
                .mapToInt(Grade::getScore)
                .sum();
        return total / grades.size();
    }

}
