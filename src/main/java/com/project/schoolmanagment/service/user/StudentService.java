package com.project.schoolmanagment.service.user;

import com.project.schoolmanagment.entity.concretes.user.User;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.payload.mappers.UserMapper;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.payload.request.user.StudentRequest;
import com.project.schoolmanagment.payload.response.abstracts.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.StudentResponse;
import com.project.schoolmanagment.repository.user.UserRepository;
import com.project.schoolmanagment.service.helper.MethodHelper;
import com.project.schoolmanagment.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final UserRepository userRepository;
    private final MethodHelper methodHelper;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleService userRoleService;

    public ResponseMessage<StudentResponse> saveStudent(StudentRequest studentRequest) {
        //check DB to see if the (user) advisor exist
        User advisorTeacher = methodHelper.isUserExist(studentRequest.getAdvisorTeacherId());
        //Check DB to see if the user is an advisor teacher
        methodHelper.checkAdvisor(advisorTeacher);
        //validate unique property validator
        uniquePropertyValidator.checkDuplicate(studentRequest.getUsername(),
                                                studentRequest.getSsn(),
                                                studentRequest.getPhoneNumber(),
                                                studentRequest.getEmail());
        //mapping to domain entity
        User student = userMapper.mapStudentRequestToUser(studentRequest);
        student.setAdvisorTeacherId(advisorTeacher.getAdvisorTeacherId());
        student.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
        student.setUserRole(userRoleService.getUserRole(RoleType.STUDENT));
        student.setActive(true);
        student.setIsAdvisor(false);
        //student numbers start with 1000
        //each student has their own number
        student.setStudentNumber(getLastNumber());

        return ResponseMessage.<StudentResponse>builder()
                .object(userMapper.mapUserToStudentResponse(userRepository.save(student)))
                .message(SuccessMessages.STUDENT_SAVE)
                .build();
    }

    private int getLastNumber(){
        if(!userRepository.findUserByRole(RoleType.STUDENT)){
            //in case of first student
            return 1000;
        }
        return userRepository.getMaxStudentNumber() + 1;
    }
}
