package com.project.schoolmanagment.service.helper;

import com.project.schoolmanagment.entity.concretes.user.User;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.exception.BadRequestException;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MethodHelper {

    private final UserRepository userRepository;

    public void isUserBuiltIn(User user){
        if(user.getBuiltIn()){
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }
    }

    public User isUserExist(Long userId){
        return userRepository.findById(userId).orElseThrow(
                ()->new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE, userId)));
    }

    public void checkRole(User user, RoleType roleType){
        if(!user.getUserRole().getRoleType().equals(roleType)){
            throw new ResourceNotFoundException(
                    String.format(ErrorMessages.NOT_FOUND_USER_USER_ROLE_MESSAGE, user.getId(), roleType));
        }
    }

    public void checkAdvisor(User user){
        if(Boolean.FALSE.equals(user.getIsAdvisor())){
            throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_ADVISOR_MESSAGE, user.getId()));
        }
    }





}
