package com.jobportal.repository;

import com.jobportal.entity.Application;
import com.jobportal.entity.Job;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByJobAndUserEmail(Job job, String userEmail);

    long countByStatus(String status);

    long count();

    List<Application> findByUserEmail(String userEmail);

    List<Application> findByJob(Job job);

    List<Application> findByJobId(Long jobId);

    @Query("SELECT j.title FROM Application a JOIN a.job j WHERE a.userEmail = :email")
    List<String> findJobTitlesByUserEmail(@Param("email") String email);


}
