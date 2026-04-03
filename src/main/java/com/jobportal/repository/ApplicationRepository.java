package com.jobportal.repository;

import com.jobportal.entity.Application;
import com.jobportal.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByJobAndUserEmail(Job job, String userEmail);

    List<Application> findByUserEmail(String userEmail);

    List<Application> findByJob(Job job);
}
