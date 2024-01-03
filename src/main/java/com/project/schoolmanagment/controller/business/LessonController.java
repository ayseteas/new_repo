package com.project.schoolmanagment.controller.business;

import com.project.schoolmanagment.entity.concretes.business.Lesson;
import com.project.schoolmanagment.payload.request.business.LessonRequest;
import com.project.schoolmanagment.payload.response.abstracts.ResponseMessage;
import com.project.schoolmanagment.payload.response.business.LessonResponse;
import com.project.schoolmanagment.service.business.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<LessonResponse> saveLesson(@Valid @RequestBody LessonRequest lessonRequest){
        return lessonService.saveLesson(lessonRequest);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage deleteLesson (@PathVariable Long id){
        return lessonService.deleteLessonById(id);
    }

    @GetMapping("/getLessonByName")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<LessonResponse> getLessonByName(@RequestParam String lessonName){
        return lessonService.getLessonByName(lessonName);
    }

    @GetMapping("/getLessonByPage")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER')")
    public Page<LessonResponse> getLessonByPage(
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "10") int size,
            @RequestParam(value = "sort",defaultValue = "lessonName") String sort,
            @RequestParam(value = "type",defaultValue = "desc") String type){
        return lessonService.getLessonByPage(page, size, sort, type);
    }

    @GetMapping("/getAllLessonByLessonId")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public Set<Lesson> getAllLessonByLessonId(@RequestParam(name = "lessonId") Set<Long> idSet){
        return lessonService.getAllLessonByLessonId(idSet);
    }

    @PutMapping("/update/{lessonId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseEntity<LessonResponse> updateLesson(@PathVariable Long lessonId,
                                                       @RequestBody @Valid LessonRequest lessonRequest){
        return ResponseEntity.ok(lessonService.updateLesson(lessonId, lessonRequest));
    }
}



