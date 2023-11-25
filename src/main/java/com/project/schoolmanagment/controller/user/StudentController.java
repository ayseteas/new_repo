package com.project.schoolmanagment.controller.user;

import com.project.schoolmanagment.payload.request.user.ChooseLessonProgramWithId;
import com.project.schoolmanagment.payload.request.user.StudentRequest;
import com.project.schoolmanagment.payload.request.user.StudentRequestWithoutPassword;
import com.project.schoolmanagment.payload.response.abstracts.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.StudentResponse;
import com.project.schoolmanagment.service.user.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseMessage<StudentResponse>> saveTeacher(@RequestBody @Valid StudentRequest studentRequest) {
        return ResponseEntity.ok(studentService.saveStudent(studentRequest));
    }


    @PatchMapping("/update")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public ResponseEntity<String> updateStudent(@RequestBody @Valid
                                                StudentRequestWithoutPassword studentRequestWithoutPassword,
                                                HttpServletRequest request) {
        return studentService.updateStudentWithoutPassword(studentRequestWithoutPassword, request);
    }


    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'ASSISTANT_MANAGER')")
    public ResponseMessage<StudentResponse> updateStudentForManagers(
            @PathVariable Long id,
            @RequestBody @Valid StudentRequest studentRequest){
        return studentService.updateStudentForManagers(id, studentRequest);
    }


    @PostMapping("/addLessonProgramToStudent")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public ResponseMessage<StudentResponse> addLessonProgram(HttpServletRequest request,
                                                             @RequestBody @Valid ChooseLessonProgramWithId chooseLessonProgramWithId) {
        return studentService.addLessonProgram(request, chooseLessonProgramWithId);
    }

    @GetMapping("/changeStatus")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'ASSISTANT_MANAGER')")
    public ResponseMessage changeStatusOfStudent(@RequestParam Long id, @RequestParam boolean status){
        return studentService.changeStatusOfStudent(id, status);
    }


}


