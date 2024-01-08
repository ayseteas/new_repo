package com.project.schoolmanagment.controller.business;

import com.project.schoolmanagment.payload.request.business.MeetingRequest;
import com.project.schoolmanagment.payload.response.abstracts.ResponseMessage;
import com.project.schoolmanagment.payload.response.business.MeetingResponse;
import com.project.schoolmanagment.service.business.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/meet")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public ResponseMessage<MeetingResponse> saveMeeting(HttpServletRequest request,
                                                        @RequestBody @Valid MeetingRequest meetingRequest) {
        return meetingService.saveMeeting(request, meetingRequest);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public List<MeetingResponse> getAll() {
        return meetingService.getAll();
    }


    @GetMapping("/getMeetById/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseMessage<MeetingResponse> getMeetingById(@PathVariable Long id) {
        return meetingService.getMeetingById(id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER')")
    public ResponseMessage deleteById(@PathVariable Long id) {
        return meetingService.deleteById(id);
    }


    @PutMapping("/update/{meetingId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER' )")
    public ResponseMessage<MeetingResponse> updateMeeting(@RequestBody @Valid MeetingRequest meetingRequest,
                                                          @PathVariable Long meetingId,
                                                          HttpServletRequest request) {
        return meetingService.updateMeeting(meetingId, meetingRequest, request);
    }

    @GetMapping("/getAllMeetByTeacher")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public ResponseEntity<List<MeetingResponse>> getAllMeetByTeacher(HttpServletRequest request) {
        return meetingService.getAllMeetByTeacher(request);
    }

    @GetMapping("/getAllMeetByStudent")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public ResponseEntity<List<MeetingResponse>> getAllMeetByStudent(HttpServletRequest request) {
        return meetingService.getAllMeetByStudent(request);
    }

    @GetMapping("/getAllMeetByPage")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<MeetingResponse> getAllMeetByPage(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size){
        return meetingService.getAllMeetByPage(page, size);
    }

    @GetMapping("/getAllMeetByAdvisorAsPage")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public ResponseEntity<Page<MeetingResponse>> getAllMeetByAdvisorAsPage(
            HttpServletRequest request,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size
    ) {
        return meetingService.getAllMeetByAdvisorAsPage(request, page, size);
    }
}
