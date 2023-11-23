package com.project.schoolmanagment.service.validator;

import com.project.schoolmanagment.entity.concretes.business.LessonProgram;
import com.project.schoolmanagment.exception.BadRequestException;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class DateTimeValidator {

    //11:35
    //15:30
    public boolean checkTime(LocalTime start, LocalTime stop){
        return start.isAfter(stop) || start.equals(stop);
    }

    public void checkTimeWithException(LocalTime start, LocalTime stop){
        if (checkTime(start, stop)){
            throw new ResourceNotFoundException(ErrorMessages.TIME_NOT_VALID_MESSAGE);
        }
    }

    //validate lesson programs in case of conflicting each other
    public void checkDuplicateLessonProgram(Set<LessonProgram> lessonPrograms){
        Set<String> uniqueLessonProgramDays = new HashSet<>();
        Set<LocalTime> existingLessonProgramStartTimes = new HashSet<>();
        Set<LocalTime> existingLessonProgramStopTimes = new HashSet<>();

        for(LessonProgram lessonProgram : lessonPrograms){
            String lessonProgramDay = lessonProgram.getDay().name();
            //if they are in the same day
            if(uniqueLessonProgramDays.contains(lessonProgramDay)){
                //validate in case of start time
                for (LocalTime startTime : existingLessonProgramStartTimes){
                    //if the start times is the same
                    if(lessonProgram.getStartTime().equals(startTime)){
                        throw new BadRequestException(ErrorMessages.LESSON_PROGRAM_ALREADY_EXIST);
                    }
                    if (lessonProgram.getStartTime().isBefore(startTime) && lessonProgram.getStopTime().isAfter(startTime)){
                        throw new BadRequestException(ErrorMessages.LESSON_PROGRAM_ALREADY_EXIST);
                    }
                }
                //validate for stop times
                for (LocalTime stopTime : existingLessonProgramStopTimes){
                    if (lessonProgram.getStartTime().isBefore(stopTime) && lessonProgram.getStopTime().isAfter(stopTime)){
                        throw new BadRequestException(ErrorMessages.LESSON_PROGRAM_ALREADY_EXIST);
                    }
                }
            }
            uniqueLessonProgramDays.add(lessonProgramDay);
            existingLessonProgramStartTimes.add(lessonProgram.getStartTime());
            existingLessonProgramStopTimes.add(lessonProgram.getStopTime());
        }
    }
}
