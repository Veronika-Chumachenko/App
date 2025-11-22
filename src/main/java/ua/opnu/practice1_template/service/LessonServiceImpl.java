package ua.opnu.practice1_template.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.opnu.practice1_template.entity.DrivingGroup;
import ua.opnu.practice1_template.entity.Lesson;
import ua.opnu.practice1_template.exception.ResourceNotFoundException;
import ua.opnu.practice1_template.repository.DrivingGroupRepository;
import ua.opnu.practice1_template.repository.LessonRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final DrivingGroupRepository groupRepository;

    @Override
    public Lesson createLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    @Override
    public List<Lesson> getLessonsByGroup(Long groupId) {
        DrivingGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + groupId));
        return lessonRepository.findByGroup(group);
    }

    @Override
    public Lesson getLessonById(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));
    }

    @Override
    public Lesson updateLesson(Long id, Lesson updatedLesson) {
        Lesson existingLesson = getLessonById(id);
        existingLesson.setTopic(updatedLesson.getTopic());
        existingLesson.setDate(updatedLesson.getDate());
        return lessonRepository.save(existingLesson);
    }


    @Override
    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }


    @Override
    public List<Lesson> getFutureLessons() {
        return lessonRepository.findByDateAfter(LocalDate.now());
    }


    @Override
    public List<Lesson> getAllLessons() {
        return (List<Lesson>) lessonRepository.findAll();
    }

}
