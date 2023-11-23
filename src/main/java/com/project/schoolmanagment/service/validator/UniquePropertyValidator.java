package com.project.schoolmanagment.service.validator;

import com.project.schoolmanagment.entity.concretes.user.User;
import com.project.schoolmanagment.exception.ConflictException;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.request.abstracts.AbstractUserRequest;
import com.project.schoolmanagment.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UniquePropertyValidator {

    private final UserRepository userRepository;

    public void checkDuplicate(String username, String ssn, String phone, String email){
        if(userRepository.existsByUsername(username)){
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_USERNAME, username));
        }
        if (userRepository.existsBySsn(ssn)){
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_SSN, ssn));
        }
        if (userRepository.existsByEmail(email)){
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_EMAIL, email));
        }
        if (userRepository.existsByPhoneNumber(phone)){
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_PHONE_NUMBER, phone));
        }
    }

    public void checkUniqueProperties(User user, AbstractUserRequest abstractUserRequest){
        String updatedUsername = "";
        String updatedSsn = "";
        String updatedPhone = "";
        String updatedEmail = "";
        boolean isChanged = false;

        // Verifies that we have the values
        if(!user.getUsername().equalsIgnoreCase(abstractUserRequest.getUsername())){
            updatedUsername = abstractUserRequest.getUsername();
            isChanged = true;
        }

        if(!user.getSsn().equalsIgnoreCase(abstractUserRequest.getSsn())){
            updatedSsn = abstractUserRequest.getSsn();
            isChanged = true;
        }

        if(!user.getPhoneNumber().equalsIgnoreCase(abstractUserRequest.getPhoneNumber())){
            updatedPhone = abstractUserRequest.getPhoneNumber();
            isChanged = true;
        }

        if(!user.getEmail().equalsIgnoreCase(abstractUserRequest.getEmail())){
            updatedEmail = abstractUserRequest.getEmail();
            isChanged = true;
        }

        if (isChanged){
            checkDuplicate(updatedUsername, updatedSsn, updatedPhone, updatedEmail);
        }
    }



}
