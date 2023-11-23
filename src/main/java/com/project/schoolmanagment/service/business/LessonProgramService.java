package com.project.schoolmanagment.service.business;

import com.project.schoolmanagment.entity.concretes.business.EducationTerm;
import com.project.schoolmanagment.entity.concretes.business.Lesson;
import com.project.schoolmanagment.entity.concretes.business.LessonProgram;
import com.project.schoolmanagment.exception.BadRequestException;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.mappers.LessonProgramMapper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.payload.request.business.LessonProgramRequest;
import com.project.schoolmanagment.payload.response.abstracts.ResponseMessage;
import com.project.schoolmanagment.payload.response.business.LessonProgramResponse;
import com.project.schoolmanagment.repository.business.LessonProgramRepository;
import com.project.schoolmanagment.service.helper.PageableHelper;
import com.project.schoolmanagment.service.validator.DateTimeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonProgramService {

    private final LessonProgramRepository lessonProgramRepository;
    private final LessonService lessonService;
    private final EducationTermService educationTermService;
    private final DateTimeValidator dateTimeValidator;
    private final LessonProgramMapper lessonProgramMapper;
    private final PageableHelper pageableHelper;

    public ResponseMessage<LessonProgramResponse> saveLessonProgram(LessonProgramRequest lessonProgramRequest) {
        //validate if the lesson ID exists
        Set<Lesson> lessons = lessonService.getAllLessonByLessonId(lessonProgramRequest.getLessonIdList());
        //validate if this education term exists
        EducationTerm educationTerm = educationTermService.isEducationTermExist(lessonProgramRequest.getEducationTermId());

        if (lessons.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.NOT_FOUND_LESSON_IN_LIST);
        }

        //validation of start time and stop time
        dateTimeValidator.checkTimeWithException(lessonProgramRequest.getStartTime(),
                                                  lessonProgramRequest.getStopTime());

        //mapping
        LessonProgram lessonProgram = lessonProgramMapper.mapLessonProgramRequestToLessonProgram(lessonProgramRequest, lessons, educationTerm);
        LessonProgram savedLessonProgram = lessonProgramRepository.save(lessonProgram);
        return ResponseMessage.<LessonProgramResponse>builder()
                .message(SuccessMessages.LESSON_PROGRAM_SAVE)
                .object(lessonProgramMapper.mapLessonProgramToLessonProgramResponse(savedLessonProgram))
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    public List<LessonProgramResponse> getAllLessonProgramByList() {
        return lessonProgramRepository
                .findAll()
                .stream()
                .map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse)
                .collect(Collectors.toList());
    }

    public Page<LessonProgramResponse> getAllLessonProgramsByPage(int page, int size, String sort, String type) {
        Pageable pageable = pageableHelper.getPageableWithProperties(page,size,sort,type);
        return lessonProgramRepository.findAll(pageable)
                .map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse);
    }


    public LessonProgramResponse getLessonProgramById(Long id) {
        //validate if the lesson program exists
        LessonProgram lessonProgram = isLessonProgramExistById(id);
        return lessonProgramMapper.mapLessonProgramToLessonProgramResponse(lessonProgram);

    }

    private LessonProgram isLessonProgramExistById(Long id){
        return lessonProgramRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_LESSON_PROGRAM_MESSAGE, id)));
    }


    public List<LessonProgramResponse> getAllUnassigned() {
        return lessonProgramRepository.findByUsers_IdNull()
                .stream()
                .map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse)
                .collect(Collectors.toList());

    }

    public List<LessonProgramResponse> getAllLessonProgramsAssigned(){
        return lessonProgramRepository.findByUsers_IdNotNull()
                .stream()
                .distinct()
                .map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse)
                .collect(Collectors.toList());
    }

    public ResponseMessage deleteLessonProgramById(Long id) {
        //validate first
        isLessonProgramExistById(id);
        lessonProgramRepository.deleteById(id);
        return ResponseMessage.builder()
                .message(SuccessMessages.LESSON_PROGRAM_DELETE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public Set<LessonProgram> getLessonProgramById(Set<Long> lessonIdSet){
        Set<LessonProgram> lessonPrograms = lessonProgramRepository.getLessonProgramByUsersUsername(lessonIdSet);
        if (lessonPrograms.isEmpty()){
            throw new BadRequestException(ErrorMessages.NOT_FOUND_LESSON_PROGRAM_MESSAGE);
        }
        return lessonPrograms;
    }
}
