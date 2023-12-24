package com.project.schoolmanagment.payload.mappers;

import com.project.schoolmanagment.entity.concretes.user.User;
import com.project.schoolmanagment.payload.request.abstracts.BaseUserRequest;
import com.project.schoolmanagment.payload.request.user.StudentRequest;
import com.project.schoolmanagment.payload.request.user.TeacherRequest;
import com.project.schoolmanagment.payload.response.user.StudentResponse;
import com.project.schoolmanagment.payload.response.user.TeacherResponse;
import com.project.schoolmanagment.payload.response.user.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // Example of Builder Design Pattern with @SuperBuilder annotation
    public User mapUserRequestToUser(BaseUserRequest userRequest){
        return User.builder()
                .username(userRequest.getUsername())
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .password(userRequest.getPassword())
                .ssn(userRequest.getSsn())
                .birthDay(userRequest.getBirthDay())
                .birthPlace(userRequest.getBirthPlace())
                .phoneNumber(userRequest.getPhoneNumber())
                .gender(userRequest.getGender())
                .email(userRequest.getEmail())
                .builtIn(userRequest.getBuiltIn())
                .build();
    }

    /**
     *
     * @param user from DB
     * @return UserResponse DTO
     */

    public UserResponse mapUserToUserResponse(User user){
        return UserResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .birthDay(user.getBirthDay())
                .birthPlace(user.getBirthPlace())
                .ssn(user.getSsn())
                .email(user.getEmail())
                .userRole(user.getUserRole().getRoleType().getName())
                .build();
    }

    public StudentResponse mapUserToStudentResponse(User student){
        return StudentResponse.builder()
                .userId(student.getId())
                .username(student.getUsername())
                .name(student.getName())
                .surname(student.getSurname())
                .birthDay(student.getBirthDay())
                .birthPlace(student.getBirthPlace())
                .phoneNumber(student.getPhoneNumber())
                .gender(student.getGender())
                .email(student.getEmail())
                .fatherName(student.getFatherName())
                .motherName(student.getMotherName())
                .studentNumber(student.getStudentNumber())
                .isActive(student.isActive())
                .lessonProgramSet(student.getLessonProgramList())
                .build();
    }

    public TeacherResponse mapUserToTeacherResponse(User teacher){
        return TeacherResponse.builder()
                .userId(teacher.getId())
                .username(teacher.getUsername())
                .name(teacher.getName())
                .surname(teacher.getSurname())
                .birthDay(teacher.getBirthDay())
                .birthPlace(teacher.getBirthPlace())
                .ssn(teacher.getSsn())
                .phoneNumber(teacher.getPhoneNumber())
                .gender(teacher.getGender())
                .email(teacher.getEmail())
                .lessonProgramSet(teacher.getLessonProgramList())
                .isAdvisorTeacher(teacher.getIsAdvisor())
                .build();
    }

    public User mapTeacherRequestToUser(TeacherRequest teacherRequest) {
        return User.builder()
                .name(teacherRequest.getName())
                .surname(teacherRequest.getSurname())
                .ssn(teacherRequest.getSsn())
                .username(teacherRequest.getUsername())
                .birthDay(teacherRequest.getBirthDay())
                .birthPlace(teacherRequest.getBirthPlace())
                .password(teacherRequest.getPassword())
                .phoneNumber(teacherRequest.getPhoneNumber())
                .email(teacherRequest.getEmail())
                .isAdvisor(teacherRequest.getIsAdvisorTeacher())
                .builtIn(teacherRequest.getBuiltIn())
                .gender(teacherRequest.getGender())
                .build();
    }

    public User mapStudentRequestToUser(StudentRequest studentRequest){
        return User.builder()
                .fatherName(studentRequest.getFatherName())
                .motherName(studentRequest.getMotherName())
                .birthDay(studentRequest.getBirthDay())
                .birthPlace(studentRequest.getBirthPlace())
                .name(studentRequest.getName())
                .surname(studentRequest.getSurname())
                .password(studentRequest.getPassword())
                .username(studentRequest.getUsername())
                .ssn(studentRequest.getSsn())
                .email(studentRequest.getEmail())
                .phoneNumber(studentRequest.getPhoneNumber())
                .gender(studentRequest.getGender())
                .builtIn(studentRequest.getBuiltIn())
                .build();
    }

    public User mapStudentRequestToUpdatedUser(StudentRequest studentRequest, Long userId){
        //we called other mapper not to write duplicated code
        User user = mapStudentRequestToUser(studentRequest);
        user.setId(userId);
        return user;
    }













}
