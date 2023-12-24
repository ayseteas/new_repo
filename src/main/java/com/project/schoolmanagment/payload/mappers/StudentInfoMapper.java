package com.project.schoolmanagment.payload.mappers;

import com.project.schoolmanagment.entity.concretes.business.EducationTerm;
import com.project.schoolmanagment.entity.concretes.business.Lesson;
import com.project.schoolmanagment.entity.concretes.business.StudentInfo;
import com.project.schoolmanagment.entity.enums.Note;
import com.project.schoolmanagment.payload.request.business.StudentInfoRequest;
import com.project.schoolmanagment.payload.request.business.UpdateStudentInfoRequest;
import com.project.schoolmanagment.payload.response.business.StudentInfoResponse;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class StudentInfoMapper {

    private final UserMapper userMapper;


    public StudentInfo mapStudentInfoRequestToStudentInfo(StudentInfoRequest studentInfoRequest,
                                                          Note note,
                                                          Double average){
        return StudentInfo.builder()
                .infoNote(studentInfoRequest.getInfoNote())
                .absentee(studentInfoRequest.getAbsentee())
                .midtermExam(studentInfoRequest.getMidtermExam())
                .finalExam(studentInfoRequest.getFinalExam())
                .examAverage(average)
                .letterGrade(note)
                .build();
    }

    public StudentInfoResponse mapStudentInfoToStudentInfoResponse(StudentInfo studentInfo){
        return StudentInfoResponse.builder()
                .lessonName(studentInfo.getLesson().getLessonName())
                .creditScore(studentInfo.getLesson().getCreditScore())
                .isCompulsory(studentInfo.getLesson().getIsCompulsory())
                .educationTerm(studentInfo.getEducationTerm().getTerm())
                .id(studentInfo.getId())
                .absentee(studentInfo.getAbsentee())
                .midtermExam(studentInfo.getMidtermExam())
                .finalExam(studentInfo.getFinalExam())
                .infoNote(studentInfo.getInfoNote())
                .note(studentInfo.getLetterGrade())
                .average(studentInfo.getExamAverage())
                .studentResponse(userMapper.mapUserToStudentResponse(studentInfo.getStudent()))
                .build();
    }

    public StudentInfo mapStudentInfoUpdateRequestToStudentInfo(UpdateStudentInfoRequest studentInfoRequest,
                                                                Long studentInfoId,
                                                                Lesson lesson,
                                                                EducationTerm educationTerm,
                                                                Note note,
                                                                Double averageNote){
        return StudentInfo.builder()
                .id(studentInfoId)
                .infoNote(studentInfoRequest.getInfoNote())
                .midtermExam(studentInfoRequest.getMidtermExam())
                .finalExam(studentInfoRequest.getFinalExam())
                .absentee(studentInfoRequest.getAbsentee())
                .lesson(lesson)
                .educationTerm(educationTerm)
                .examAverage(averageNote)
                .letterGrade(note)
                .build();
    }












    }





