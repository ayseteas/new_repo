package com.project.schoolmanagment.repository.business;

import com.project.schoolmanagment.entity.concretes.business.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    boolean existsByLessonNameEqualsIgnoreCase(String lessonName);

    //Optional will handle nullPointerException for us
    Optional<Lesson> getLessonByLessonName(String lessonName);

}
