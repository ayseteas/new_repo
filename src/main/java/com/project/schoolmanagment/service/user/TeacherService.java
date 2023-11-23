package com.project.schoolmanagment.service.user;

import com.project.schoolmanagment.entity.concretes.business.LessonProgram;
import com.project.schoolmanagment.entity.concretes.user.User;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.exception.BadRequestException;
import com.project.schoolmanagment.payload.mappers.UserMapper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.payload.request.user.ChooseLessonTeacherRequest;
import com.project.schoolmanagment.payload.request.user.TeacherRequest;
import com.project.schoolmanagment.payload.response.abstracts.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.TeacherResponse;
import com.project.schoolmanagment.payload.response.user.UserResponse;
import com.project.schoolmanagment.repository.user.UserRepository;
import com.project.schoolmanagment.service.business.LessonProgramService;
import com.project.schoolmanagment.service.helper.MethodHelper;
import com.project.schoolmanagment.service.validator.DateTimeValidator;
import com.project.schoolmanagment.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final LessonProgramService lessonProgramService;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final MethodHelper methodHelper;
    private final DateTimeValidator dateTimeValidator;

    public ResponseMessage<TeacherResponse> saveTeacher(TeacherRequest teacherRequest) {
        //validate lesson program set
        Set<LessonProgram> lessonProgramSet = lessonProgramService.getLessonProgramById(teacherRequest.getLessonProgramIdList());

        //validate unique properties
        uniquePropertyValidator.checkDuplicate(teacherRequest.getUsername(),
                teacherRequest.getSsn(),
                teacherRequest.getPhoneNumber(),
                teacherRequest.getEmail());


        //mapping to domain object
        User teacher = userMapper.mapTeacherRequestToUser(teacherRequest);
        //map missing properties
        teacher.setUserRole(userRoleService.getUserRole(RoleType.TEACHER));
        teacher.setLessonProgramList(lessonProgramSet);
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        //is advisory teacher
        teacher.setIsAdvisor(teacherRequest.getIsAdvisorTeacher());
        User savedTeacher = userRepository.save(teacher);
        return ResponseMessage.<TeacherResponse>builder()
                .message(SuccessMessages.TEACHER_SAVE)
                .object(userMapper.mapUserToTeacherResponse(savedTeacher))
                .build();
    }

    public ResponseMessage<TeacherResponse> updateTeacherForManagers(TeacherRequest teacherRequest, Long userId) {
        //validate if this user exist
        User user = methodHelper.isUserExist(userId);
        //validate the role
        methodHelper.checkRole(user, RoleType.TEACHER);
        Set<LessonProgram> lessonPrograms = lessonProgramService.getLessonProgramById(teacherRequest.getLessonProgramIdList());
        //validate unique properties
        uniquePropertyValidator.checkUniqueProperties(user, teacherRequest);
        User updatedTeacher = userMapper.mapTeacherRequestToUser(teacherRequest);
        //Setting the missing properties
        updatedTeacher.setId(user.getId());
        updatedTeacher.setPassword(passwordEncoder.encode(teacherRequest.getPassword()));
        updatedTeacher.setLessonProgramList(lessonPrograms);
        updatedTeacher.setUserRole(userRoleService.getUserRole(RoleType.TEACHER));
        User savedTeacher = userRepository.save(updatedTeacher);
        return ResponseMessage.<TeacherResponse>builder()
                .message(SuccessMessages.TEACHER_UPDATE)
                .object(userMapper.mapUserToTeacherResponse(savedTeacher))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<TeacherResponse> addLessonProgram(ChooseLessonTeacherRequest teacherRequest) {
        //validate if teacher exists
        User teacher = methodHelper.isUserExist(teacherRequest.getTeacherId());
        //validate if the user is a teacher
        methodHelper.checkRole(teacher, RoleType.TEACHER);
        //the lesson programs that need to be updated
        Set<LessonProgram> lessonPrograms = lessonProgramService.getLessonProgramById(teacherRequest.getLessonProgramId());
        Set<LessonProgram> teacherExistingLessonProgram = teacher.getLessonProgramList();
        if(lessonPrograms.equals(teacherExistingLessonProgram)){
            throw new BadRequestException(ErrorMessages.LESSON_PROGRAM_THE_SAME);
        }
        //validating the new ones
        dateTimeValidator.checkDuplicateLessonProgram(lessonPrograms);
        teacherExistingLessonProgram.addAll(lessonPrograms);
        //validating all lesson programs
        dateTimeValidator.checkDuplicateLessonProgram(teacherExistingLessonProgram);
        //if no validation issue we are setting the teachers lesson program
        teacher.setLessonProgramList(teacherExistingLessonProgram);


        User updatedTeacher = userRepository.save(teacher);

        return ResponseMessage.<TeacherResponse>builder()
                .message(SuccessMessages.LESSON_PROGRAM_ADD_TO_TEACHER)
                .httpStatus(HttpStatus.OK)
                .object(userMapper.mapUserToTeacherResponse(updatedTeacher))
                .build();
    }

    public List<UserResponse> getAllAdvisorTeacher() {
        return userRepository.findAllByAdvisorTeacher(true)
                .stream()
                .map(userMapper::mapUserToUserResponse)
                .collect(Collectors.toList());
    }

    public ResponseMessage<UserResponse> deleteAdvisorTeacherById(Long id) {
        //Is user exist
        User teacher = methodHelper.isUserExist(id);
        //is user a teacher
        methodHelper.checkRole(teacher, RoleType.TEACHER);
        //is user an advisorId
        methodHelper.checkAdvisor(teacher);

        teacher.setIsAdvisor(false);
        userRepository.save(teacher);
        List<User> allStudents = userRepository.findByAdvisorTeacherId(id);
        //We need to set the deleted advisor teacher info from the students
        if(!allStudents.isEmpty()){
            allStudents.forEach(student ->student.setAdvisorTeacherId(null));
        }

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.ADVISOR_TEACHER_DELETE)
                .object(userMapper.mapUserToUserResponse(teacher))
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
