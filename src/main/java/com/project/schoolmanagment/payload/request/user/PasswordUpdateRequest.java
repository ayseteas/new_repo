package com.project.schoolmanagment.payload.request.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordUpdateRequest {

    @NotNull(message = "Please provide old password")
    private String oldPassword;

    @NotNull(message = "Please provide new password")
    private String newPassword;
}
