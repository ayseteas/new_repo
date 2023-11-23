package com.project.schoolmanagment.controller.user;

import com.project.schoolmanagment.payload.request.user.StudentRequest;
import com.project.schoolmanagment.payload.request.user.TeacherRequest;
import com.project.schoolmanagment.payload.response.abstracts.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.StudentResponse;
import com.project.schoolmanagment.payload.response.user.TeacherResponse;
import com.project.schoolmanagment.service.user.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseMessage<StudentResponse>> saveTeacher(@RequestBody @Valid StudentRequest studentRequest){
        return ResponseEntity.ok(studentService.saveStudent(studentRequest));
    }

    public ResponseEntity<String> updateStudent (@RequestBody @Valid)







}
