package com.project.schoolmanagment.payload.response.abstracts;

import com.project.schoolmanagment.entity.enums.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class BaseUserResponse {

    private Long userId;
    private String username;
    private String name;
    private String surname;
    private LocalDate birthDay;
    private String ssn;
    private String birthPlace;
    private String phoneNumber;
    private Gender gender;
    private String email;
    private String userRole;
}
