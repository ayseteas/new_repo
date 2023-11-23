package com.project.schoolmanagment.payload.mappers;

import com.project.schoolmanagment.entity.concretes.business.EducationTerm;
import com.project.schoolmanagment.payload.request.business.EducationTermRequest;
import com.project.schoolmanagment.payload.response.business.EducationTermResponse;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class EducationTermMapper {

    // DTO -> Domain Object Mapper
    public EducationTerm mapEducationTermRequestToEducationTerm(EducationTermRequest educationTermRequest){
        return EducationTerm.builder()
                .term(educationTermRequest.getTerm())
                .startDate(educationTermRequest.getStartDate())
                .endDate(educationTermRequest.getEndDate())
                .lastRegistrationDate(educationTermRequest.getLastRegistrationDate())
                .build();
    }

    //Domain Object -> DTO mapper
    public EducationTermResponse mapEducationTermToEducationTermResponse(EducationTerm educationTerm){
        return EducationTermResponse.builder()
                .id(educationTerm.getId())
                .term(educationTerm.getTerm())
                .startDate(educationTerm.getStartDate())
                .endDate(educationTerm.getEndDate())
                .lastRegistrationDate(educationTerm.getLastRegistrationDate())
                .build();
    }


//    public EducationTerm mapEducationTermRequestToEducationTermForUpdate(Long id, EducationTermRequest educationTermRequest){
//        return mapEducationTermRequestToEducationTerm(educationTermRequest)
//                .toBuilder()
//                .id(id)
//                .build();
//    }


    public EducationTerm mapEducationTermRequestToEducationTermForUpdate(Long id, EducationTermRequest educationTermRequest) {
        EducationTerm term = mapEducationTermRequestToEducationTerm(educationTermRequest);
        term.setId(id);
        return term;
    }













}
