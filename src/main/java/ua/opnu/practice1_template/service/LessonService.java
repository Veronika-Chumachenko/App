package ua.opnu.practice1_template.service;

import ua.opnu.practice1_template.entity.Lesson;
import java.util.List;

public interface LessonService {
    Lesson createLesson(Lesson lesson);
    List<Lesson> getLessonsByGroup(Long groupId);
    void deleteLesson(Long id);
    Lesson getLessonById(Long id);
    Lesson updateLesson(Long id, Lesson updatedLesson);
    List<Lesson> getFutureLessons();
    List<Lesson> getAllLessons();
}

