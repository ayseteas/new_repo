package com.project.schoolmanagment.controller.business;

import com.project.schoolmanagment.entity.concretes.business.Meet;
import com.project.schoolmanagment.payload.request.business.MeetingRequest;
import com.project.schoolmanagment.payload.response.abstracts.ResponseMessage;
import com.project.schoolmanagment.payload.response.business.MeetingResponse;
import com.project.schoolmanagment.service.business.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/meet")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public ResponseMessage<MeetingResponse> saveMeeting(HttpServletRequest request,
                                                        @RequestBody @Valid MeetingRequest meetingRequest){
        return meetingService.saveMeeting(request, meetingRequest);
    }


}
