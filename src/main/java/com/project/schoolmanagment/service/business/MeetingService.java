package com.project.schoolmanagment.service.business;

import com.project.schoolmanagment.entity.concretes.business.Meet;
import com.project.schoolmanagment.entity.concretes.user.User;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.exception.ConflictException;
import com.project.schoolmanagment.payload.mappers.MeetingMapper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.payload.request.business.MeetingRequest;
import com.project.schoolmanagment.payload.response.abstracts.ResponseMessage;
import com.project.schoolmanagment.payload.response.business.MeetingResponse;
import com.project.schoolmanagment.repository.business.MeetingRepository;
import com.project.schoolmanagment.service.helper.MethodHelper;
import com.project.schoolmanagment.service.user.UserService;
import com.project.schoolmanagment.service.validator.DateTimeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserService userService;
    private final MethodHelper methodHelper;
    private final DateTimeValidator dateTimeValidator;
    private final MeetingMapper meetingMapper;

    public ResponseMessage<MeetingResponse> saveMeeting(HttpServletRequest request, MeetingRequest meetingRequest) {
        String username = (String) request.getAttribute("username");

        //validate username
        User advisorTeacher = methodHelper.isUserExistByUsername(username);
        //validate is advisor teacher
        methodHelper.checkAdvisor(advisorTeacher);

        dateTimeValidator.checkTimeWithException(meetingRequest.getStartTime(), meetingRequest.getStopTime());

        checkMeetingConflicts(advisorTeacher.getId(),
                                meetingRequest.getDate(),
                                meetingRequest.getStartTime(),
                                meetingRequest.getStopTime());

        List<User> students = userService.findUsersByIdArray(meetingRequest.getStudentIds());
        //validate if they are a student
        for (User user:students){
            methodHelper.checkRole(user, RoleType.STUDENT);
        }

        Meet meet = meetingMapper.mapMeetRequestToMeet(meetingRequest);
        meet.setStudentList(students);
        meet.setAdvisoryTeacher(advisorTeacher);
        Meet savedMeet = meetingRepository.save(meet);

        return ResponseMessage.<MeetingResponse>builder()
                .message(SuccessMessages.MEET_SAVE)
                .object(meetingMapper.mapMeetToMeetingResponse(savedMeet))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    private void checkMeetingConflicts(Long userId, LocalDate date, LocalTime startTime, LocalTime stopTime) {
        List<Meet> meets;

        //Student or advisor
        if (methodHelper.isUserExist(userId).getIsAdvisor()) {
            meets = meetingRepository.findByAdvisoryTeacher_IdEquals(userId);
        } else {
            meets = meetingRepository.findByStudentList_IdEquals(userId);

        }

        //conflict validation
        for (Meet meet : meets) {
            LocalTime existingStartTime = meet.getStartTime();
            LocalTime existingStopTime = meet.getStopTime();

            if (meet.getDate().equals(date)
                    && (
                    (startTime.isAfter(existingStartTime) && startTime.isBefore(existingStopTime) ||
                            startTime.isAfter(existingStartTime) && stopTime.isBefore(existingStopTime) ||
                            startTime.isBefore(existingStartTime) && stopTime.isAfter(existingStopTime) ||
                            startTime.equals(existingStartTime) || stopTime.equals(existingStopTime)))) {

                throw new ConflictException(ErrorMessages.MEET_HOURS_CONFLICT);

            }

        }
    }









    }










