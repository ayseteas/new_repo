package com.project.schoolmanagment.repository.business;

import com.project.schoolmanagment.entity.concretes.business.StudentInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentInfoRepository extends JpaRepository<StudentInfo, Long> {

    //@Query("select s from StudentInfo s where User.id IN :studentId")
    List<StudentInfo> getAllByStudentId_Id(Long studentId);

    boolean existsByIdEquals(Long id);

    @Query("SELECT s from StudentInfo s where s.teacher.username= ?1")
    Page<StudentInfo> findByTeacherUsername(String username, Pageable pageable);

    @Query("SELECT s from StudentInfo s where s.student.username= ?1")
    Page<StudentInfo> findByStudentUsername(String username, Pageable pageable);


    @Query("SELECT (count (s)>0) FROM StudentInfo s WHERE s.student.id = ?1")
    boolean existByStudentId(Long studentId);



    }







