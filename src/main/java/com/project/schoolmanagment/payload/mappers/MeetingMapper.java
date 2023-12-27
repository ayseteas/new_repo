package com.project.schoolmanagment.payload.mappers;

import com.project.schoolmanagment.entity.concretes.business.Meet;
import com.project.schoolmanagment.payload.request.business.MeetingRequest;
import com.project.schoolmanagment.payload.response.business.MeetingResponse;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class MeetingMapper {

    public Meet mapMeetRequestToMeet(MeetingRequest meetingRequest){
        return  Meet.builder()
                .date(meetingRequest.getDate())
                .startTime(meetingRequest.getStartTime())
                .stopTime(meetingRequest.getStopTime())
                .description(meetingRequest.getDescription())
                .build();
    }

    public MeetingResponse mapMeetToMeetingResponse(Meet meet){
        return MeetingResponse.builder()
                .id(meet.getId())
                .date(meet.getDate())
                .startTime(meet.getStartTime())
                .stopTime(meet.getStopTime())
                .description(meet.getDescription())
                .advisorTeacherId(meet.getAdvisoryTeacher().getAdvisorTeacherId())
                .teacherName(meet.getAdvisoryTeacher().getName())
                .teacherSsn(meet.getAdvisoryTeacher().getSsn())
                .teacherUsername(meet.getAdvisoryTeacher().getUsername())
                .students(meet.getStudentList())
                .build();
    }

    public Meet mapMeetUpdateRequestToMeet(MeetingRequest request, Long meetId){
        return Meet.builder()
                .id(meetId)
                .startTime(request.getStartTime())
                .stopTime(request.getStopTime())
                .date(request.getDate())
                .description(request.getDescription())
                .build();
    }



}
