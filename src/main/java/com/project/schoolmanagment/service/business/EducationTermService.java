package com.project.schoolmanagment.service.business;


import com.project.schoolmanagment.entity.concretes.business.EducationTerm;
import com.project.schoolmanagment.exception.ConflictException;
import com.project.schoolmanagment.exception.ResourceNotFoundException;
import com.project.schoolmanagment.payload.mappers.EducationTermMapper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.messages.SuccessMessages;
import com.project.schoolmanagment.payload.request.business.EducationTermRequest;
import com.project.schoolmanagment.payload.response.abstracts.ResponseMessage;
import com.project.schoolmanagment.payload.response.business.EducationTermResponse;
import com.project.schoolmanagment.repository.business.EducationTermRepository;
import com.project.schoolmanagment.service.helper.PageableHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EducationTermService {

    private final EducationTermRepository educationTermRepository;
    private final EducationTermMapper educationTermMapper;
    private final PageableHelper pageableHelper;

    public ResponseMessage<EducationTermResponse> saveEducationTerm(EducationTermRequest educationTermRequest) {
        //first validation
        validateEducationTerms(educationTermRequest);
        //mapping + saving
        EducationTerm saveEducationTerm = educationTermRepository
                .save(educationTermMapper.mapEducationTermRequestToEducationTerm(educationTermRequest));
        //mapping + returning DTO
        return ResponseMessage.<EducationTermResponse>builder()
                .message(SuccessMessages.EDUCATION_TERM_SAVE)
                .object(educationTermMapper.mapEducationTermToEducationTermResponse(saveEducationTerm))
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    //sub-validator for education term
    private void validateEducationTermDatesForRequest(EducationTermRequest educationTermRequest){
        //registration > start

        if(educationTermRequest.getLastRegistrationDate().isAfter(educationTermRequest.getStartDate())){
            throw new ConflictException(ErrorMessages.EDUCATION_START_DATE_IS_EARLIER_THAN_LAST_REGISTRATION_DATE);
        }
        //start -> start
        if(educationTermRequest.getEndDate().isBefore(educationTermRequest.getStartDate())){
            throw new ConflictException(ErrorMessages.EDUCATION_END_DATE_IS_EARLIER_THAN_START_DATE);
        }
    }

    //main validator for education term
    private void validateEducationTerms(EducationTermRequest educationTermRequest){
        validateEducationTermDatesForRequest(educationTermRequest);
        //Only one education term can exist in a year (fall OR spring)
        if (educationTermRepository.existByTermAndYear(educationTermRequest.getTerm(), educationTermRequest.getStartDate().getYear())){
            throw new ResourceNotFoundException(ErrorMessages.EDUCATION_TERM_IS_ALREADY_EXIST_BY_TERM_AND_YEAR_MESSAGE);
        }
        //while we create an education term, there should not be any conflict between the education terms

    }

    public List<EducationTermResponse> getAllEducationTerms() {
        return educationTermRepository.findAll()
                .stream()
                .map(educationTermMapper::mapEducationTermToEducationTermResponse)
                .collect(Collectors.toList());
    }

    public EducationTermResponse findEducationTermById(Long id) {
        EducationTerm educationTerm = isEducationTermExist(id);
        return educationTermMapper.mapEducationTermToEducationTermResponse(educationTerm);
    }

    public EducationTerm isEducationTermExist(Long id){
       return educationTermRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.EDUCATION_TERM_NOT_FOUND_MESSAGE, id)));
    }

    public ResponseMessage<EducationTermResponse> updateEducationTerm(Long id, EducationTermRequest educationTermRequest) {
        isEducationTermExist(id);

        //check the expected dates are correct
        validateEducationTermDatesForRequest(educationTermRequest);

        EducationTerm updatedEducationTerm = educationTermRepository.save(educationTermMapper.mapEducationTermRequestToEducationTermForUpdate(id, educationTermRequest));

        return ResponseMessage.<EducationTermResponse>
                builder()
                .message(SuccessMessages.EDUCATION_TERM_UPDATE)
                .object(educationTermMapper.mapEducationTermToEducationTermResponse(updatedEducationTerm))
                .httpStatus(HttpStatus.OK)
                .build();

    }

    public ResponseMessage<?> deleteById(Long id) {
        isEducationTermExist(id);
        educationTermRepository.deleteById(id);
        return ResponseMessage.builder()
                .message(SuccessMessages.EDUCATION_TERM_DELETE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public Page<EducationTermResponse> getAllEducationTermsByPage(int page, int size, String sort, String type) {
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);
        return educationTermRepository.findAll(pageable)
                .map(educationTermMapper::mapEducationTermToEducationTermResponse);
    }
}
