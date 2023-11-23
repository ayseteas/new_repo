package com.project.schoolmanagment.controller.user;

import com.project.schoolmanagment.payload.request.user.ChooseLessonTeacherRequest;
import com.project.schoolmanagment.payload.request.user.TeacherRequest;
import com.project.schoolmanagment.payload.response.abstracts.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.TeacherResponse;
import com.project.schoolmanagment.payload.response.user.UserResponse;
import com.project.schoolmanagment.service.user.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;


    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseMessage<TeacherResponse>> saveTeacher(@Valid @RequestBody TeacherRequest teacherRequest){
        return ResponseEntity.ok(teacherService.saveTeacher(teacherRequest));
    }

    @PutMapping("/update/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'ASSISTANT_MANAGER')")
    public ResponseMessage<TeacherResponse> updateTeacherForManagers(@RequestBody @Valid TeacherRequest teacherRequest,
                                                                     @PathVariable Long userId){
        return teacherService.updateTeacherForManagers(teacherRequest, userId);
    }

    @PostMapping("/addLessonProgram")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<TeacherResponse> addLessonProgram(@RequestBody @Valid ChooseLessonTeacherRequest teacherRequest){
        return teacherService.addLessonProgram(teacherRequest);
    }

    @GetMapping("/getAllAdvisorTeacher")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public List<UserResponse> getAllAdvisorTeacher(){
        return teacherService.getAllAdvisorTeacher();
    }

    @DeleteMapping("/deleteAdvisorTeacherById/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<UserResponse> deleteAdvisorTeacherById(@PathVariable Long id){
        return teacherService.deleteAdvisorTeacherById(id);
    }





}
