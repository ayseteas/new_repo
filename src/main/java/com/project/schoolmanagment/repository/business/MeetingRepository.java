package com.project.schoolmanagment.repository.business;

import com.project.schoolmanagment.entity.concretes.business.Meet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepository extends JpaRepository<Meet, Long> {

}
