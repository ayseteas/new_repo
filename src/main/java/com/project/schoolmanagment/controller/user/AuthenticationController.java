package com.project.schoolmanagment.controller.user;

import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.payload.request.user.LoginRequest;
import com.project.schoolmanagment.payload.request.user.PasswordUpdateRequest;
import com.project.schoolmanagment.payload.response.user.LoginResponse;
import com.project.schoolmanagment.service.user.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse>authenticateUser(@Valid @RequestBody LoginRequest request){
        return authenticationService.authenticateUser(request);
    }

    @PatchMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody PasswordUpdateRequest passwordUpdateRequest,
                                                HttpServletRequest request){
        authenticationService.updatePassword(passwordUpdateRequest,request);
        String response = SuccessMessages.PASSWORD_CHANGED_RESPONSE_MESSAGE;
        return ResponseEntity.ok(response);
    }

}
