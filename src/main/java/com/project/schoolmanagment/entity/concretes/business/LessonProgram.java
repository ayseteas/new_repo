package com.project.schoolmanagment.entity.concretes.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.schoolmanagment.entity.concretes.user.User;
import com.project.schoolmanagment.entity.enums.Day;
import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Day day;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "HH:mm", timezone = "US")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "HH:mm", timezone = "US")
    private LocalTime stopTime;

    @ManyToMany
    @JoinTable(
            name = "lesson_program_lesson",
            joinColumns = @JoinColumn(name = "lessonprogram_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id")
    )
    private Set<Lesson>lessons;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private EducationTerm educationTerm;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToMany(mappedBy = "lessonProgramList", fetch = FetchType.EAGER)
    private Set<User>users;

    @PreRemove
    private void removeLessonProgramFromUser(){
        users.forEach(user -> user.getLessonProgramList().remove(this));
    }





}
